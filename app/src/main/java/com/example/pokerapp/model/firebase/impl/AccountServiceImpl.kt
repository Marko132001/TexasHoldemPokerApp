package com.example.pokerapp.model.firebase.impl

import android.util.Log
import com.example.pokerapp.model.UserData
import com.example.pokerapp.model.firebase.AccountService
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.IllegalArgumentException

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AccountService {

    companion object{
        private const val DEFAULT_NUMBER_OF_CHIPS: Int = 30000
    }

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val currentUser: Flow<UserData>
        get() = callbackFlow {
            val listener = firestore.collection("users")
                .document(currentUserId).addSnapshotListener { value, error ->
                    this.trySend(value?.toObject<UserData>() ?: UserData())
                }
            awaitClose { listener.remove(); this.cancel() }
        }

    override suspend fun authenticate(email: String, password: String) {
        try{
            auth.signInWithEmailAndPassword(email, password).await()
        }
        catch (e: FirebaseAuthException) {
            throw FirebaseAuthException(e.errorCode, "Invalid login credentials.")
        }
        catch (e: FirebaseNetworkException){
            throw FirebaseNetworkException("Connection error. Check your internet connection.")
        }
    }

    override suspend fun createAccount(email: String, password: String, username: String) {
        Log.d("FIREBASE", "Creating new user")
        try {
            if(!firestore.collection("users")
                .whereEqualTo("username", username).get().await().isEmpty
            ) {
                throw IllegalArgumentException("Provided username is already taken.")
            }

            auth.createUserWithEmailAndPassword(email, password).await().let {
                saveUserData(
                    it.user?.uid ?: "",
                    username,
                    null
                )
            }
        }
        catch (e: FirebaseAuthException) {
            throw FirebaseAuthException(e.errorCode, "This E-mail is already registered.")
        }
        catch (e: FirebaseFirestoreException){
            throw FirebaseFirestoreException("Error when accessing database.", e.code)
        }
        catch (e: FirebaseNetworkException){
            throw FirebaseNetworkException("Connection error. Check your internet connection.")
        }
    }

    private fun saveUserData(userId: String, username: String, avatarUrl: String?)
    {
        Log.d("FIREBASE", "Saving user data")
        val userData = UserData(userId, username, DEFAULT_NUMBER_OF_CHIPS, avatarUrl)
        val userRef = firestore.collection("users").document(userId)

        userRef.set(userData)
    }

    override suspend fun deleteAcount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        Log.d("FIREBASE", "Signing out")
        auth.signOut()
    }
}
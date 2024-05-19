package com.example.pokerapp.model.firebase.impl

import android.util.Log
import com.example.pokerapp.model.User
import com.example.pokerapp.model.UserData
import com.example.pokerapp.model.firebase.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AccountService {

    companion object{
        private val DEFAULT_NUMBER_OF_CHIPS: Int = 1000
    }

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await().user
    }

    override suspend fun createAccount(email: String, password: String, username: String) {
        Log.d("FIREBASE", "Creating new user")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    saveUserData(
                        auth.currentUser?.uid ?: "",
                        username,
                        null
                    )
                }
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
        auth.signOut()
    }
}
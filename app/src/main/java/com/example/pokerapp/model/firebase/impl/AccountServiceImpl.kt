package com.example.pokerapp.model.firebase.impl

import android.net.Uri
import android.util.Log
import com.example.pokerapp.model.UserData
import com.example.pokerapp.model.firebase.AccountService
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.IllegalArgumentException

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
) : AccountService {

    companion object{
        private const val DEFAULT_NUMBER_OF_CHIPS: Int = 30000
    }

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val currentUser: Flow<UserData>
        get() = callbackFlow {
            val user = fetchUserData()
            this.trySend(user)
            awaitClose { this.cancel() }
        }

    override val users: Flow<List<UserData>>
        get() = firestore.collection("users")
            .orderBy("chipAmount", Query.Direction.DESCENDING)
            .dataObjects()

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

    private suspend fun fetchUserData(): UserData {
        return firestore.collection("users")
            .document(currentUserId).get().await().toObject<UserData>() ?: UserData()
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

    override suspend fun addImageToFirebaseStorage(imageUri: Uri): String {
        try {
            val downloadUrl = storage.reference
                .child("images").child("$currentUserId.jpg")
                .putFile(imageUri).await()
                .storage.downloadUrl.await()

            addImageUrlToFirestore(downloadUrl)
        }
        catch (e: FirebaseFirestoreException){
            throw FirebaseFirestoreException("Error when accessing database.", e.code)
        }
        catch (e: Exception){
            throw Exception("Image upload failed")
        }

        return "Image upload complete"
    }

    private suspend fun addImageUrlToFirestore(downloadUrl: Uri) {
        firestore.collection("users").document(currentUserId)
            .update("avatarUrl", downloadUrl).await()
    }

    override suspend fun changeUsername(newUsername: String): String {
        try {
            firestore.collection("users").document(currentUserId)
                .update("username", newUsername)
        }
        catch (e: FirebaseFirestoreException){
            throw FirebaseFirestoreException("Error while changing username.", e.code)
        }

        return "Username changed successfully."
    }

    override suspend fun deleteAcount(avatarUrl: String?) {
        try {
            firestore.collection("users").document(currentUserId)
                .delete().await()

            if(avatarUrl != null) {
                storage.reference.child("images").child("$currentUserId.jpg")
                    .delete().await()
            }

            auth.currentUser!!.delete().await()
        }
        catch (e: FirebaseAuthInvalidUserException){
            throw FirebaseAuthInvalidUserException("This user is invalid.", "")
        }
        catch (e: FirebaseAuthRecentLoginRequiredException){
            throw FirebaseAuthRecentLoginRequiredException(
                "Recent login is required. Reauthenticate and try again.", ""
            )
        }
        catch (e: FirebaseFirestoreException){
            throw FirebaseFirestoreException(
                "Error occurred when deleting firestore user data.", e.code
            )
        }
    }

    override suspend fun signOut() {
        Log.d("FIREBASE", "Signing out")
        auth.signOut()
    }
}
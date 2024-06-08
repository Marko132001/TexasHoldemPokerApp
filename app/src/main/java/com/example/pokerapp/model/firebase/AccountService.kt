package com.example.pokerapp.model.firebase

import android.net.Uri
import com.example.pokerapp.model.UserData
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val currentUser: Flow<UserData>
    suspend fun authenticate(email: String, password: String)
    suspend fun createAccount(email: String, password: String, username: String)
    suspend fun addImageToFirebaseStorage(imageUri: Uri): String
    suspend fun changeUsername(newUsername: String): String
    suspend fun deleteAcount()
    suspend fun signOut()
}
package com.example.pokerapp.model.firebase

import com.example.pokerapp.model.User
import com.example.pokerapp.model.UserData
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    suspend fun getCurrentUserData(): UserData?
    suspend fun authenticate(email: String, password: String)
    suspend fun createAccount(email: String, password: String, username: String)
    suspend fun deleteAcount()
    suspend fun signOut()
}
package com.example.pokerapp.model.firebase

import com.example.pokerapp.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun createAccount(email: String, password: String, username: String)
    suspend fun deleteAcount()
    suspend fun signOut()
}
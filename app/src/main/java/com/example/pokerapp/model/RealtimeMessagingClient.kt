package com.example.pokerapp.model

import kotlinx.coroutines.flow.Flow

interface RealtimeMessagingClient {
    fun getGameStateStream(): Flow<GameState>
    suspend fun sendAction(action: PlayerAction)
    suspend fun sendUserData(userData: UserData)
    suspend fun sendRebuyData(userData: UserData)
    suspend fun close()
}
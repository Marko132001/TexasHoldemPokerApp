package com.example.projectapp.model

import kotlinx.coroutines.flow.Flow

interface RealtimeMessagingClient {
    fun getGameStateStream(): Flow<GameState>
    suspend fun sendAction(action: PlayerAction)
    suspend fun close()
}
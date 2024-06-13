package com.example.pokerapp.model

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class KtorRealtimeMessagingClient(
    private val client: HttpClient
): RealtimeMessagingClient {

    private var session: WebSocketSession? = null

    override fun getGameStateStream(): Flow<GameState> {
        return flow {
            session = client.webSocketSession {
                url("ws://poorly-secure-fox.ngrok-free.app:80/play")
            }
            val gameStates = session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .mapNotNull { Json.decodeFromString<GameState>(it.readText()) }
            emitAll(gameStates)
        }
    }

    override suspend fun sendAction(action: PlayerAction) {
        session?.outgoing?.send(
            Frame.Text("make_turn#${Json.encodeToString(action)}")
        )
    }

    override suspend fun sendUserData(userData: UserData) {
        for(i in 1..10){
            Log.d("KTOR", "Sending client data...")
            delay(500)
            val response = session?.outgoing?.trySend(
                Frame.Text("user_data#${Json.encodeToString(userData)}")
            )
            if (response != null && response.isSuccess) {
                break
            }
        }
    }

    override suspend fun sendRebuyData(userData: UserData) {
        session?.outgoing?.send(
            Frame.Text("user_rebuy#${Json.encodeToString(userData)}")
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }

}
package com.example.pokerapp.screens.game

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokerapp.model.GameState
import com.example.pokerapp.model.PlayerAction
import com.example.pokerapp.model.RealtimeMessagingClient
import com.example.pokerapp.model.UserData
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val client: RealtimeMessagingClient,
    private val accountService: AccountService
): AppViewModel() {

    init {
        Log.d("GAMEVIEWMODEL", "Sending client ID...")
        launchCatching {
            accountService.getCurrentUserData()?.let { clientUserData(it) }
        }
    }

    val clientUserId = accountService.currentUserId
    var opponentPlayersPositions by mutableStateOf(arrayOfNulls<String?>(5))

    var raiseAmount by mutableIntStateOf(50)
    var isRaiseSlider by mutableStateOf(false)

    var timerProgress by mutableFloatStateOf(1.0f)

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting = _isConnecting.asStateFlow()

    private val _showConnectionError = MutableStateFlow(false)
    val showConnectionError = _showConnectionError.asStateFlow()

    val state = client
        .getGameStateStream()
        .onStart { _isConnecting.value = true }
        .onEach { _isConnecting.value = false }
        .catch { t -> _showConnectionError.value = t is ConnectException }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState())

    fun playerAction(playerState: String, raiseAmount: Int) {
        launchCatching {
            client.sendAction(PlayerAction(playerState, raiseAmount))
        }
    }

    private suspend fun clientUserData(clientUserData: UserData) {
         client.sendUserData(clientUserData)
    }

    suspend fun timerCountdown() {
        for (i in 100 downTo 0) {
            timerProgress = i.toFloat() / 100
            delay(100)
        }
    }

    override fun onCleared() {
        super.onCleared()
        launchCatching {
            client.close()
        }
    }
}
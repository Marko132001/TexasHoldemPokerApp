package com.example.pokerapp.screens.game

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.pokerapp.model.GameState
import com.example.pokerapp.model.PlayerAction
import com.example.pokerapp.model.RealtimeMessagingClient
import com.example.pokerapp.model.UserData
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.navigation.GAME_SCREEN_PARAMS
import com.example.pokerapp.navigation.HOME_SCREEN
import com.example.pokerapp.screens.AppViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import java.net.ConnectException
import java.util.Collections

@HiltViewModel(assistedFactory = GameViewModel.DetailViewModelFactory::class)
class GameViewModel @AssistedInject constructor(
    @Assisted private val buyInValue: Int,
    private val client: RealtimeMessagingClient,
    private val accountService: AccountService
): AppViewModel() {

    @AssistedFactory
    interface DetailViewModelFactory {
        fun create(buyInValue: Int): GameViewModel
    }

    init {
        launchCatching {
            accountService.currentUser.collect {
                clientUser.value = it
                clientUserData(UserData(it.userId, it.username, buyInValue))
            }
        }
    }

    val clientUser = mutableStateOf(UserData())
    val clientUserId = accountService.currentUserId
    var playerSeatPositions by mutableStateOf(arrayOfNulls<String?>(5))

    val minBuyIn = 1000
    val maxBuyIn = 5000

    var raiseAmount by mutableIntStateOf(50)
    var isRaiseSlider by mutableStateOf(false)

    var timerProgress by mutableFloatStateOf(1.0f)

    private val _isConnecting = MutableStateFlow(false)
    val isConnecting = _isConnecting.asStateFlow()

    private val _showConnectionError = MutableStateFlow(false)
    val showConnectionError = _showConnectionError.asStateFlow()

    var isOnPlayClicked by mutableStateOf(false)

    val state = client
        .getGameStateStream()
        .onStart { _isConnecting.value = true }
        .onEach { _isConnecting.value = false }
        .catch { t -> _showConnectionError.value = t is ConnectException }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState())

    fun onPlayClick(openAndPopUp: (String, String) -> Unit, buyInValue: Int) {
        isOnPlayClicked = true
        launchCatching {
            client.sendRebuyData(
                UserData(clientUser.value.userId, clientUser.value.username, buyInValue)
            )
        }
    }

    fun onQuitGameClick(openAndPopUp: (String, String) -> Unit) {
        onCleared()
        openAndPopUp(HOME_SCREEN, GAME_SCREEN_PARAMS)
    }

    fun playerAction(playerState: String, raiseAmount: Int) {
        launchCatching {
            client.sendAction(PlayerAction(playerState, raiseAmount))
        }
    }

    private suspend fun clientUserData(clientUserData: UserData) {
        Log.d("GAMEVIEWMODEL", "${clientUserData.userId}, ${clientUserData.chipAmount}")
        client.sendUserData(clientUserData)
    }

    fun setCircularPlayerSeatOrder(){
        val playerPositionsList = state.value.playerSeatPositions.toList()
        val clientPlayerIndex = playerPositionsList.indexOf(clientUserId)

        Collections.rotate(playerPositionsList, -clientPlayerIndex)

        playerSeatPositions = playerPositionsList.toTypedArray()
    }

    override fun onCleared() {
        super.onCleared()
        launchCatching {
            client.close()
        }
    }
}
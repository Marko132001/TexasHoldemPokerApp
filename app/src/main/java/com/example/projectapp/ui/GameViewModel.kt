package com.example.projectapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectapp.data.GameRound
import com.example.projectapp.model.Game
import com.example.projectapp.model.Player
import com.example.projectapp.model.User
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val game: Game = Game()
    private lateinit var round: GameRound

    var raiseAmount by mutableIntStateOf(50)
    var isRaiseSlider by mutableStateOf(false)
    var timerProgress by mutableFloatStateOf(1.0f)
    private lateinit var timerJob: Job

    //private val handler = Handler(Looper.getMainLooper())

    init {
        initGame()
    }

    private suspend fun timerCountdown() {
        for (i in 100 downTo 0) {
            timerProgress = i.toFloat() / 100
            delay(100)
        }
    }

    private fun initGame() {
        val user1 = User("User1")
        val user2 = User("User2")
        val user3 = User("User3")
        val user4 = User("User4")
        val user5 = User("User5")

        val player1 = Player(user1, chipBuyInAmount = 500)
        val player2 = Player(user2, chipBuyInAmount = 900)
        val player3 = Player(user3, chipBuyInAmount = 700)
        val player4 = Player(user4, chipBuyInAmount = 1000)
        val player5 = Player(user5, chipBuyInAmount = 1200)

        game.playerJoin(player1)
        game.playerJoin(player2)
        game.playerJoin(player3)
        game.playerJoin(player4)
        game.playerJoin(player5)

        resetGame()
    }

    private fun resetGame() {
        round = GameRound.PREFLOP

        game.preflopRoundInit()

        _uiState.value = GameUiState(
            round = round,
            potAmount = game.potAmount,
            bigBlind = game.bigBlind,
            currentHighBet = game.currentHighBet,
            dealerButtonPos = game.dealerButtonPos,
            players = game.players,
            currentPlayerIndex = game.currentPlayerIndex,
            communityCards = game.showStreet(round).map { it.cardLabel },
            isRaiseEnabled = true,
            isCallEnabled = true,
            isCheckEnabled = false,
            isFoldEnabled = true
        )

        timerJob = viewModelScope.launch {
            timerCountdown()
            handleFoldAction()
        }
    }

    private fun updateBettingRound() {
        timerJob.cancel()
        val nextRound = game.nextRoundInit(round)

        if(nextRound == GameRound.SHOWDOWN){
            round = nextRound
            game.assignChipsToWinner(game.rankCardHands())

            _uiState.update { currentState ->
                currentState.copy(
                    round = round,
                    communityCards = game.showStreet(round).map { it.cardLabel }
                )
            }

            timerJob = viewModelScope.launch {
                delay(4000)
                timerJob.cancel()
                resetGame()
            }

        }
        else if(nextRound != round) {
            round = nextRound

            _uiState.update { currentState ->
                currentState.copy(
                    round = round,
                    communityCards = game.showStreet(round).map { it.cardLabel },
                    isRaiseEnabled = true
                )
            }
        }

        updateAvailableActions()
        if(round != GameRound.SHOWDOWN){
            timerJob = viewModelScope.launch {
                timerCountdown()
                if(_uiState.value.isCheckEnabled) {
                    handleCheckAction()
                }
                else{
                    handleFoldAction()
                }
            }
        }
    }

    private fun updateAvailableActions() {
        isRaiseSlider = false
        _uiState.update { currentState ->
            currentState.copy(
                potAmount = game.potAmount,
                players = game.players,
                currentHighBet = game.currentHighBet,
                currentPlayerIndex = game.currentPlayerIndex,
                isCheckEnabled =
                    game.currentHighBet <= game.players[game.currentPlayerIndex].playerBet
                            && round != GameRound.SHOWDOWN,
                isRaiseEnabled =
                    game.currentHighBet < game.players[game.currentPlayerIndex].chipBuyInAmount
                            && round != GameRound.SHOWDOWN,
                isCallEnabled =
                    game.currentHighBet > game.players[game.currentPlayerIndex].playerBet
                            && round != GameRound.SHOWDOWN,
                isFoldEnabled = round != GameRound.SHOWDOWN
            )
        }
    }

    fun handleCallAction() {
        game.updatePot(game.players[game.currentPlayerIndex].call(game.currentHighBet))

        updateBettingRound()
    }

    fun handleRaiseAction() {
        game.updatePot(game.players[game.currentPlayerIndex]
            .raise(game.currentHighBet, raiseAmount)
        )
        game.currentHighBet = game.players[game.currentPlayerIndex].playerBet
        game.raiseFlag = true

        updateBettingRound()
    }

    fun handleCheckAction() {
        game.players[game.currentPlayerIndex].check()

        updateBettingRound()
    }

    fun handleFoldAction() {
        game.players[game.currentPlayerIndex].fold()

        updateBettingRound()
    }


}
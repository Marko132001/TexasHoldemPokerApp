package com.example.projectapp.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projectapp.data.GameRound
import com.example.projectapp.model.Game
import com.example.projectapp.model.Player
import com.example.projectapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val game: Game = Game()
    private lateinit var round: GameRound

    var raiseAmount by mutableIntStateOf(50)
    var isRaiseSlider by mutableStateOf(false)

    init {
        initGame()
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
            potAmount = game.potAmount,
            bigBlind = game.bigBlind,
            currentHighBet = game.currentHighBet,
            dealerButtonPos = game.dealerButtonPos,
            players = game.players,
            currentPlayerIndex = game.currentPlayerIndex,
            communityCards = game.showStreet(round).map { it.cardLabel }
        )
    }

    private fun updateBettingRound() {

        val nextRound = game.nextRoundInit(round)

        if(nextRound != round) {
            round = nextRound

            _uiState.update { currentState ->
                currentState.copy(
                    players = game.players,
                    currentHighBet = game.currentHighBet,
                    currentPlayerIndex = game.currentPlayerIndex,
                    communityCards = game.showStreet(round).map { it.cardLabel },
                    isRaiseEnabled = true
                )
            }

            Log.d("ROUND", "$round")

            if (round == GameRound.SHOWDOWN) {
                game.assignChipsToWinner(game.rankCardHands())


                //TODO: Display winner in UI

                resetGame()
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
                    game.currentHighBet <= game.players[game.currentPlayerIndex].playerBet,
                isRaiseEnabled =
                    (game.currentHighBet < game.players[game.currentPlayerIndex].chipBuyInAmount),
                isCallEnabled =
                    game.currentHighBet > game.players[game.currentPlayerIndex].playerBet,
            )
        }
    }

    fun handleCallAction() {
        game.updatePot(game.players[game.currentPlayerIndex].call(game.currentHighBet))

        updateBettingRound()
        updateAvailableActions()
    }

    fun handleRaiseAction() {
        game.updatePot(game.players[game.currentPlayerIndex]
            .raise(game.currentHighBet, raiseAmount)
        )
        game.currentHighBet = game.players[game.currentPlayerIndex].playerBet
        game.raiseFlag = true

        updateBettingRound()
        updateAvailableActions()
    }

    fun handleCheckAction() {
        game.players[game.currentPlayerIndex].check()

        updateBettingRound()
        updateAvailableActions()
    }

    fun handleFoldAction() {
        game.players[game.currentPlayerIndex].fold()

        updateBettingRound()
        updateAvailableActions()
    }


}
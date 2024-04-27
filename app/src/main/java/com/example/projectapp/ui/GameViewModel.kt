package com.example.projectapp.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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

    //TODO: Handle user raise amount input
    var raiseAmount by mutableIntStateOf(50)
        private set

    init {
        initGame()
    }

    private fun initGame() {
        val user1 = User("User1")
        val user2 = User("User2")

        val player1 = Player(user1, chipBuyInAmount = 500)
        val player2 = Player(user2, chipBuyInAmount = 900)

        game.playerJoin(player1)
        game.playerJoin(player2)

        resetGame()
    }

    private fun resetGame() {
        round = GameRound.PREFLOP

        game.preflopRoundInit()

        _uiState.value = GameUiState(
            holeCards = Pair(
                game.players[game.currentPlayerIndex].getHoleCards().first.cardLabel,
                game.players[game.currentPlayerIndex].getHoleCards().second.cardLabel
            ),
            communityCards = game.showStreet(round).map { it.cardLabel },
            isRaiseEnabled =
                if (game.currentHighBet >= game.players[game.currentPlayerIndex].chipBuyInAmount)
                    false
                else
                    true
        )
    }

    private fun updateBettingRound() {
        if(game.isCurrentRoundFinished()) {
            round = round.nextRound()
            game.streetRoundInit()
            round = game.nextRoundInit(round)

            _uiState.update { currentState ->
                currentState.copy(
                    communityCards = game.showStreet(round).map { it.cardLabel },
                )
            }

            Log.d("ROUND", "$round")

            if(round == GameRound.SHOWDOWN) {
                game.assignChipsToWinner(game.rankCardHands())

                //TODO: Display winner in UI

                resetGame()
            }
        }

        Log.d("Player1", game.players[0].toString())
        Log.d("Player2", game.players[1].toString())
    }

    private fun updateAvailableActions() {
        _uiState.update { currentState ->
            currentState.copy(
                isCheckEnabled =
                if (game.currentHighBet > game.players[game.currentPlayerIndex].playerBet)
                    false
                else
                    true,
                isRaiseEnabled =
                if (game.currentHighBet >= game.players[game.currentPlayerIndex].chipBuyInAmount)
                    false
                else
                    true
            )
        }
    }

    fun handleCallAction() {
        Log.d("CALL", "${game.players[game.currentPlayerIndex].user.username} called for ${game.currentHighBet}$")
        game.updatePot(game.players[game.currentPlayerIndex].call(game.currentHighBet))
        game.iterateCurrentPlayerIndex()

        updateAvailableActions()
        updateBettingRound()
    }

    fun handleRaiseAction() {
        Log.d("RAISE", "${game.players[game.currentPlayerIndex].user.username} raised for $raiseAmount$")
        game.updatePot(game.players[game.currentPlayerIndex]
            .raise(game.currentHighBet, raiseAmount)
        )
        game.currentHighBet = game.players[game.currentPlayerIndex].playerBet
        game.raiseFlag = true
        game.iterateCurrentPlayerIndex()

        updateAvailableActions()
    }

    fun handleCheckAction() {
        Log.d("CHECK", "${game.players[game.currentPlayerIndex].user.username} checked")
        game.players[game.currentPlayerIndex].check()
        game.iterateCurrentPlayerIndex()

        updateAvailableActions()
        updateBettingRound()
    }

    fun handleFoldAction() {
        Log.d("FOLD", "${game.players[game.currentPlayerIndex].user.username} folded")
        game.players[game.currentPlayerIndex].fold()
        game.iterateCurrentPlayerIndex()

        updateAvailableActions()
        updateBettingRound()
    }


}
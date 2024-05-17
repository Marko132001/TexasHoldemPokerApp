package com.example.pokerapp.model

import com.example.pokerapp.data.GameRound
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val round: GameRound = GameRound.PREFLOP,
    val potAmount: Int = 0,
    val bigBlind: Int = 0,
    val currentHighBet: Int = 0,
    val dealerButtonPos: Int = 0,
    val players: List<PlayerDataState> = listOf(),
    val currentPlayerIndex: Int = 0,
    val communityCards: List<String> = listOf(),
    val isCallEnabled: Boolean = true,
    val isRaiseEnabled: Boolean = true,
    val isCheckEnabled: Boolean = false,
    val isFoldEnabled: Boolean = true
)

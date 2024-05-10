package com.example.projectapp.ui

import com.example.projectapp.model.Player


data class GameUiState(
    val potAmount: Int = 0,
    val bigBlind: Int = 0,
    val currentHighBet: Int = 0,
    val dealerButtonPos: Int = 0,
    val players: List<Player> = listOf(),
    val currentPlayerIndex: Int = 0,
    val communityCards: List<String> = listOf(),
    val isCallEnabled: Boolean = true,
    val isRaiseEnabled: Boolean = true,
    val isCheckEnabled: Boolean = false
)

package com.example.projectapp.ui

import com.example.projectapp.data.PlayerState


data class GameUiState(
    val potAmount: Int = 0,
    val bigBlind: Int = 0,
    val dealerButtonPos: Int = 0,
    val currentPlayerChips: Int = 0,
    val playerUserNames: List<String> = listOf(),
    val playersBuyInChips: List<Int> = listOf(),
    val playerBets: List<Int> = listOf(),
    val playerStates: List<PlayerState> = listOf(),
    val holeCards: Pair<String, String> = Pair("", ""),
    val communityCards: List<String> = listOf(),
    val isCallEnabled: Boolean = true,
    val isRaiseEnabled: Boolean = true,
    val isCheckEnabled: Boolean = false
)

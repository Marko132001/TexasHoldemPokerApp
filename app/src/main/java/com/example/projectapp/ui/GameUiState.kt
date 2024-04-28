package com.example.projectapp.ui


data class GameUiState(
    val potAmount: Int = 0,
    val bigBlind: Int = 0,
    val currentPlayerChips: Int = 0,
    val playerBets: List<Int> = listOf(),
    val holeCards: Pair<String, String> = Pair("", ""),
    val communityCards: List<String> = listOf(),
    val isCallEnabled: Boolean = true,
    val isRaiseEnabled: Boolean = true,
    val isCheckEnabled: Boolean = false
)

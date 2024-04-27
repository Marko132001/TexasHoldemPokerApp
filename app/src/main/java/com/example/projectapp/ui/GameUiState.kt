package com.example.projectapp.ui


data class GameUiState(
    val holeCards: Pair<String, String> = Pair("", ""),
    val communityCards: List<String> = listOf(),
    val isCallEnabled: Boolean = true,
    val isRaiseEnabled: Boolean = true,
    val isCheckEnabled: Boolean = false,
)

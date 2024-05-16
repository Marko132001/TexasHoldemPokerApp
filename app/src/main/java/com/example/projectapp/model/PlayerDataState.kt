package com.example.projectapp.model

import com.example.projectapp.data.PlayerState
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDataState(
    val username: String,
    val chipBuyInAmount: Int,
    val holeCards: Pair<String, String>,
    val playerHandRank: String,
    val playerState: PlayerState,
    val playerBet: Int
)

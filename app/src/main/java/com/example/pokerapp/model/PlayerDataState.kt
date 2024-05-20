package com.example.pokerapp.model

import com.example.pokerapp.data.PlayerState
import kotlinx.serialization.Serializable

@Serializable
data class PlayerDataState(
    val userId: String,
    val username: String,
    val avatarUrl: String?,
    val chipBuyInAmount: Int,
    val holeCards: Pair<String, String>,
    val playerHandRank: String,
    val playerState: PlayerState,
    val playerBet: Int
)

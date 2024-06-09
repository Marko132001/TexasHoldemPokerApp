package com.example.pokerapp.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerAction(val playerState: String, val raiseAmount: Int)

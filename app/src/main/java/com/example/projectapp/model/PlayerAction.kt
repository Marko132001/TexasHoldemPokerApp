package com.example.projectapp.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerAction(val playerState: String, val raiseAmount: Int)

package com.example.projectapp.model

import com.example.projectapp.data.GameRound
import com.example.projectapp.data.PlayingCard

interface TableActions {
    fun generateCommunityCards()
    fun generateHoleCards()
    fun showStreet(gameRound: GameRound): Any
    fun updatePot(playerBet: Int)
    fun updateDealerButtonPos()
    fun getPlayerRolePos(playerRoleOffset: Int): Int
    fun rankCardHands()

}
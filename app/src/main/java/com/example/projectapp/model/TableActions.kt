package com.example.projectapp.model

import com.example.projectapp.data.PlayingCard

interface TableActions {
    fun updatePlayerList()
    fun generateCommunityCards()
    fun generateHoleCards()
    fun showFlop(): List<PlayingCard>
    fun showTurn(): PlayingCard
    fun showRiver(): PlayingCard
    fun updatePot(playerBet: Int)
    fun updateDealerButtonPos()
    fun rankCardHands()

}
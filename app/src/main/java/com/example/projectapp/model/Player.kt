package com.example.projectapp.model

import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard

class Player(val user: User, var chipBuyInAmount: Int) {

    private lateinit var holeCards: Pair<PlayingCard, PlayingCard>
    var playerHandRank: Pair<HandRankings, Int> = Pair(HandRankings.HIGH_CARD, 7462)
    var playerState: PlayerState = PlayerState.NONE
    var playerBet: Int = 0

    fun assignHoleCards(holeCardsAssigned: Pair<PlayingCard, PlayingCard>){
        holeCards = holeCardsAssigned
    }

    fun getHoleCards(): Pair<PlayingCard, PlayingCard> {

        return holeCards
    }

    fun assignChips(chipAmount: Int){
        chipBuyInAmount += chipAmount
    }

    fun call(currentHighBet: Int): Int {
        val betDifference = currentHighBet - playerBet
        if(betDifference >= chipBuyInAmount){
            playerState = PlayerState.ALL_IN
            val allInCall = chipBuyInAmount
            chipBuyInAmount = 0
            playerBet += allInCall

            return allInCall
        }

        playerState = PlayerState.CALL
        playerBet += betDifference
        chipBuyInAmount -= betDifference

        return betDifference
    }

    fun raise(currentHighBet: Int, raiseAmount: Int): Int {
        playerState = PlayerState.RAISE
        val betDifference = (currentHighBet - playerBet)
        playerBet += betDifference + raiseAmount
        chipBuyInAmount -= betDifference + raiseAmount

        return betDifference + raiseAmount
    }

    fun check() {
        playerState = PlayerState.CHECK
    }

    fun fold() {
        playerState = PlayerState.FOLD
    }

    fun paySmallBlind(smallBlindValue: Int): Int {
        chipBuyInAmount -= smallBlindValue
        playerBet = smallBlindValue

        return smallBlindValue
    }

    fun payBigBlind(bigBlindValue: Int): Int {
        chipBuyInAmount -= bigBlindValue
        playerBet = bigBlindValue

        return bigBlindValue
    }

    override fun toString(): String {
        return "Player(chipAmount=$chipBuyInAmount, playerBet=$playerBet), playerState=$playerState"
    }
}
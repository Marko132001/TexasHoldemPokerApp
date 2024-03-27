package com.example.projectapp.model

import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard

class Player(val user: User, private var chipBuyInAmount: Int): PlayerRoundActions {

    private lateinit var holeCards: Pair<PlayingCard, PlayingCard>
    var playerHandRank: HandRankings = HandRankings.HIGH_CARD
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

    override fun call(currentHighBet: Int): Int {
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

    override fun raise(currentHighBet: Int, raiseAmount: Int): Int {
        playerState = PlayerState.RAISE
        val betDifference = (currentHighBet - playerBet)
        playerBet += betDifference + raiseAmount
        chipBuyInAmount -= betDifference + raiseAmount

        return betDifference + raiseAmount
    }

    override fun paySmallBlind(smallBlindValue: Int): Int {
        chipBuyInAmount -= smallBlindValue
        playerBet = smallBlindValue

        return smallBlindValue
    }

    override fun payBigBlind(bigBlindValue: Int): Int {
        chipBuyInAmount -= bigBlindValue
        playerBet = bigBlindValue

        return bigBlindValue
    }

    override fun check() {
        playerState = PlayerState.CHECK
    }

    override fun fold() {
        playerState = PlayerState.FOLD
    }

    override fun toString(): String {
        return "Player(chipAmount=$chipBuyInAmount, playerBet=$playerBet), playerState=$playerState"
    }
}
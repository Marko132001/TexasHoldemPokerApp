package com.example.projectapp.model

import android.util.Log
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

            Log.d("PLAYER", "${user.username} went ALL IN for ${allInCall} chips")

            return allInCall
        }

        playerState = PlayerState.CALL
        playerBet += betDifference
        chipBuyInAmount -= betDifference

        Log.d("PLAYER", "${user.username} CALLED for ${betDifference} chips")

        return betDifference
    }

    fun raise(currentHighBet: Int, raiseAmount: Int): Int {
        //TODO: Check the ALL_IN condition
        if(raiseAmount == chipBuyInAmount) {
            playerState = PlayerState.ALL_IN
            val allInCall = chipBuyInAmount
            chipBuyInAmount = 0
            playerBet += allInCall

            Log.d("PLAYER", "${user.username} went ALL IN for ${allInCall} chips")

            return allInCall
        }
        playerState = PlayerState.RAISE
        val betDifference = (currentHighBet - playerBet)
        playerBet += betDifference + raiseAmount
        chipBuyInAmount -= betDifference + raiseAmount

        Log.d("PLAYER",
            "${user.username} made a BET for ${betDifference + raiseAmount} chips")

        return betDifference + raiseAmount
    }

    fun check() {
        playerState = PlayerState.CHECK

        Log.d("PLAYER", "${user.username} CHECKED")
    }

    fun fold() {
        playerState = PlayerState.FOLD

        Log.d("PLAYER", "${user.username} FOLDED")
    }

    fun paySmallBlind(smallBlindValue: Int): Int {
        chipBuyInAmount -= smallBlindValue
        playerBet = smallBlindValue

        Log.d("PLAYER", "${user.username} is SMALL BLIND")

        return smallBlindValue
    }

    fun payBigBlind(bigBlindValue: Int): Int {
        chipBuyInAmount -= bigBlindValue
        playerBet = bigBlindValue

        Log.d("PLAYER", "${user.username} is BIG BLIND")

        return bigBlindValue
    }

    override fun toString(): String {
        return "Player(chipAmount=$chipBuyInAmount, playerBet=$playerBet), playerState=$playerState"
    }
}
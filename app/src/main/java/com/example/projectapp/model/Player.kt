package com.example.projectapp.model

import android.util.Log
import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard
import kotlinx.serialization.Serializable

@Serializable
class Player(val username: String, var chipBuyInAmount: Int) {

    private lateinit var holeCards: Pair<PlayingCard, PlayingCard>
    var playerHandRank: Pair<HandRankings, Int> = Pair(HandRankings.HIGH_CARD, 7462)
    var playerState: PlayerState = PlayerState.INACTIVE
    var playerBet: Int = 0

    fun assignHoleCards(holeCardsAssigned: Pair<PlayingCard, PlayingCard>){
        holeCards = holeCardsAssigned
    }

    fun getHoleCards(): Pair<PlayingCard, PlayingCard> {

        return holeCards
    }

    fun getHoleCardsLabels(): Pair<String, String> {
        return Pair(holeCards.first.cardLabel, holeCards.second.cardLabel)
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

            Log.d("PLAYER", "${username} went ALL IN for ${allInCall} chips")

            return allInCall
        }

        playerState = PlayerState.CALL
        playerBet += betDifference
        chipBuyInAmount -= betDifference

        Log.d("PLAYER", "${username} CALLED for ${betDifference} chips")

        return betDifference
    }

    fun raise(currentHighBet: Int, raiseAmount: Int): Int {
        val betDifference = (currentHighBet - playerBet)
        if(raiseAmount == chipBuyInAmount - betDifference) {
            playerState = PlayerState.ALL_IN

            Log.d("PLAYER", "${username} went ALL IN for ${betDifference + raiseAmount} chips")
        }
        else {
            playerState = PlayerState.RAISE

            Log.d("PLAYER",
                "${username} made a BET for ${betDifference + raiseAmount} chips")
        }

        playerBet += (betDifference + raiseAmount)
        chipBuyInAmount -= (betDifference + raiseAmount)

        return betDifference + raiseAmount
    }

    fun check() {
        playerState = PlayerState.CHECK

        Log.d("PLAYER", "${username} CHECKED")
    }

    fun fold() {
        playerState = PlayerState.FOLD

        Log.d("PLAYER", "${username} FOLDED")
    }

    fun paySmallBlind(smallBlindValue: Int): Int {
        chipBuyInAmount -= smallBlindValue
        playerBet = smallBlindValue

        Log.d("PLAYER", "${username} is SMALL BLIND")

        return smallBlindValue
    }

    fun payBigBlind(bigBlindValue: Int): Int {
        chipBuyInAmount -= bigBlindValue
        playerBet = bigBlindValue

        Log.d("PLAYER", "${username} is BIG BLIND")

        return bigBlindValue
    }

    override fun toString(): String {
        return "Player(chipAmount=$chipBuyInAmount, playerBet=$playerBet), playerState=$playerState"
    }
}
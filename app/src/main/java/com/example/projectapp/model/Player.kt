package com.example.projectapp.model

import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard

class Player(val user: User, private var chipAmount: Int): PlayerRoundActions {

    private lateinit var holeCards: Pair<PlayingCard, PlayingCard>
    private var playerState: PlayerState = PlayerState.NONE

    fun assignHoleCards(holeCardsAssigned: Pair<PlayingCard, PlayingCard>){

        if(!this::holeCards.isInitialized){
            holeCards = holeCardsAssigned
        }
    }

    override fun call(currentBet: Int): Int {
        if(currentBet >= chipAmount){
            playerState = PlayerState.ALL_IN
            var callAmount = chipAmount
            chipAmount = 0
            return callAmount
        }
        chipAmount -= currentBet

        return currentBet
    }

    override fun raise(currentBet: Int, raiseAmount: Int): Int {
        chipAmount -= currentBet + raiseAmount
        return currentBet + raiseAmount
    }

    override fun paySmallBlind(smallBlindValue: Int): Int {
        chipAmount -= smallBlindValue
        return smallBlindValue
    }

    override fun payBigBlind(bigBlindValue: Int): Int {
        chipAmount -= bigBlindValue
        return bigBlindValue
    }

    override fun check() {
        playerState = PlayerState.CHECK
    }

    override fun fold() {
        playerState = PlayerState.FOLD
    }
}
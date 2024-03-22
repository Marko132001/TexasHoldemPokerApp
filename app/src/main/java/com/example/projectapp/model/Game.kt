package com.example.projectapp.model

import com.example.projectapp.data.GameRound
import com.example.projectapp.data.PlayingCard
import com.example.projectapp.data.Rank
import com.example.projectapp.data.Suit
import kotlin.random.Random

class Game(var players: MutableList<Player>): TableActions {
    var potAmount: Int = 0
    var currentHighBet: Int = 0
    val smallBlind: Int = 25
    val bigBlind: Int = 50
    private var communityCards: MutableList<PlayingCard> = mutableListOf()
    var dealerButtonPos: Int = -1

    object CardConstants {
        const val COMMUNITY_CARDS = 5
        const val HOLE_CARDS = 2
        const val FLOP_CARDS_START = 0
        const val FLOP_CARDS_END = 3
        const val TURN_CARD_INDEX = 3
        const val RIVER_CARD_INDEX = 4
    }

    object PlayerRoleOffsets {
        const val SMALL_BLIND = 1
        const val BIG_BLIND = 2
        const val UNDER_THE_GUN = 3
    }

    override fun updatePlayerList() {
        TODO(
            "If lobby is not full (capacity=5) ? add player : throw exception message" +
                    "Remove player from list if leaving the lobby"
        )
    }

    override fun generateCommunityCards() {
        communityCards = PlayingCard.entries.shuffled()
            .take(CardConstants.COMMUNITY_CARDS).toMutableList()
    }

    override fun generateHoleCards() {
        val generatedHoleCards = PlayingCard.entries
            .shuffled().take(CardConstants.HOLE_CARDS * players.size)

        for((index, player) in players.withIndex()){
            player.assignHoleCards(Pair(generatedHoleCards[index*2], generatedHoleCards[index*2 + 1]))
        }
    }

    override fun showStreet(gameRound: GameRound): Any {
        return when(gameRound){
            GameRound.FLOP -> communityCards
                .subList(CardConstants.FLOP_CARDS_START, CardConstants.FLOP_CARDS_END)

            GameRound.TURN -> communityCards[CardConstants.TURN_CARD_INDEX]
            GameRound.RIVER -> communityCards[CardConstants.RIVER_CARD_INDEX]
            else -> communityCards
        }
    }

    override fun updatePot(playerBet: Int) {
        potAmount += playerBet
    }

    override fun updateDealerButtonPos() {
        dealerButtonPos = (dealerButtonPos + 1) % players.size
    }

    override fun getPlayerRolePos(playerRoleOffset: Int): Int {
        return (dealerButtonPos + playerRoleOffset) % players.size
    }


    override fun rankCardHands() {
        TODO("Implement the appropriate card ranking algorithm")
    }

    override fun toString(): String {
        return "Game(potAmount=$potAmount, currentHighBet=$currentHighBet)"
    }
}
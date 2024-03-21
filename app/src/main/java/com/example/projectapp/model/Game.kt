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
    var gameRound: GameRound = GameRound.PREFLOP
    private var communityCards: MutableList<PlayingCard> = mutableListOf<PlayingCard>()
    var dealerButtonPos: Int = -1

    object CardConstants {
        const val COMMUNITY_CARDS = 5
        const val HOLE_CARDS = 2
        const val FLOP_CARDS_START = 0
        const val FLOP_CARDS_END = 3
        const val TURN_CARD_INDEX = 3
        const val RIVER_CARD_INDEX = 4
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

    override fun showFlop(): List<PlayingCard> {
        return communityCards.subList(CardConstants.FLOP_CARDS_START, CardConstants.FLOP_CARDS_END)
    }

    override fun showTurn(): PlayingCard {
        return communityCards[CardConstants.TURN_CARD_INDEX]
    }

    override fun showRiver(): PlayingCard {
        return communityCards[CardConstants.RIVER_CARD_INDEX]
    }

    override fun updatePot(playerBet: Int) {
        potAmount += playerBet
    }

    override fun updateDealerButtonPos() {
        dealerButtonPos = (dealerButtonPos + 1) % players.size
    }

    override fun rankCardHands() {
        TODO("Implement the appropriate card ranking algorithm")
    }

    override fun toString(): String {
        return "Game(potAmount=$potAmount, currentHighBet=$currentHighBet)"
    }
}
package com.example.projectapp.model

import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayingCard
import com.example.projectapp.data.Tables

class CardHandEvaluator() {

    fun getHandRanking(cards: MutableList<PlayingCard>): HandRankings {
        val handRankValue = evaluate(cards)

        if(handRankValue > 6185)
            return HandRankings.HIGH_CARD
        else if (handRankValue > 3325)
            return HandRankings.ONE_PAIR
        else if (handRankValue > 2467)
            return HandRankings.TWO_PAIR
        else if (handRankValue > 1609)
            return HandRankings.THREE_OF_A_KIND
        else if (handRankValue > 1599)
            return HandRankings.STRAIGHT
        else if (handRankValue > 322)
            return HandRankings.FLUSH
        else if (handRankValue > 166)
            return HandRankings.FULL_HOUSE
        else if (handRankValue > 10)
            return HandRankings.FOUR_OF_A_KIND
        else if (handRankValue > 1)
            return HandRankings.STRAIGHT_FLUSH

        return HandRankings.ROYAL_FLUSH
    }

    private fun evaluate(cards: MutableList<PlayingCard>): Int {
        // Binary representations of each card
        val c1: Int = cards[0].value
        val c2: Int = cards[1].value
        val c3: Int = cards[2].value
        val c4: Int = cards[3].value
        val c5: Int = cards[4].value

        // Calculate index in the flushes/unique table
        val index = c1 or c2 or c3 or c4 or c5 shr 16

        // Flushes, including straight flushes
        if (c1 and c2 and c3 and c4 and c5 and 0xF000 != 0) {
            return Tables.Flushes.TABLE[index].toInt()
        }

        // Straight and high card hands
        val value = Tables.Unique.TABLE[index].toInt()
        if (value != 0) {
            return value
        }

        // Remaining cards
        val product = (c1 and 0xFF) * (c2 and 0xFF) * (c3 and 0xFF) * (c4 and 0xFF) * (c5 and 0xFF)
        return Tables.Hash.Values.TABLE[hash(product)].toInt()
    }

    private fun hash(key: Int): Int {
        var key = key
        key += -0x16e555cb
        key = key xor (key ushr 16)
        key += key shl 8
        key = key xor (key ushr 4)
        return key + (key shl 2) ushr 19 xor Tables.Hash.Adjust.TABLE[key ushr 8 and 0x1FF].toInt()
    }
}
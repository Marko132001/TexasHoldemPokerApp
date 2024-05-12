package com.example.projectapp.model

import android.util.Log
import com.example.projectapp.data.GameRound
import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard

class Game() {

    var players: MutableList<Player> = mutableListOf()
    var potAmount: Int = 0
    var currentHighBet: Int = 0
    val smallBlind: Int = 25
    val bigBlind: Int = 50
    private var communityCards: MutableList<PlayingCard> = mutableListOf()
    var dealerButtonPos: Int = -1
    var currentPlayerIndex: Int = -1
    private var endRoundIndex: Int = -1
    var raiseFlag: Boolean = false

    object CardConstants {
        const val COMMUNITY_CARDS = 5
        const val HOLE_CARDS = 2
        const val FLOP_CARDS_START = 0
        const val FLOP_CARDS_END = 3
        const val TURN_CARD_INDEX = 4
        const val RIVER_CARD_INDEX = 5
        const val HAND_COMBINATION = 3
    }

    fun playerJoin(newPlayer: Player) {
        if(players.size < 5){
            players.add(newPlayer)
        }
        else{
            println("The game you are trying to join is currently full.")
        }
    }

    fun playerQuit(player: Player) {
        if(players.size > 0){
            if(players.indexOf(player) == endRoundIndex){
                endRoundIndex = getPlayerRolePosition(endRoundIndex)
            }
            if(players.indexOf(player) == currentPlayerIndex){
                currentPlayerIndex = getPlayerRolePosition(currentPlayerIndex)
            }
            players.remove(player)
            println("Player ${player.user.username} has left the game.")
        }
    }

    fun preflopRoundInit() {
        raiseFlag = false

        Log.d("GAME", "Initializing preflop round...")

        currentPlayerIndex = -1
        endRoundIndex = -1

        players.forEach {
            player ->
                if(player.chipBuyInAmount == 0){
                    player.playerState = PlayerState.SPECTATOR
                }
                else {
                    player.playerState = PlayerState.INACTIVE
                }
                player.playerHandRank = Pair(HandRankings.HIGH_CARD, 7462)
                player.playerBet = 0
        }

        dealerButtonPos = getPlayerRolePosition(dealerButtonPos)

        val cards: List<PlayingCard> = shuffleCardsDeck()
        generateHoleCards(cards)
        generateCommunityCards(cards)

        val smallBlindIndex = getPlayerRolePosition(dealerButtonPos)
        val bigBlindIndex = getPlayerRolePosition(smallBlindIndex)
        currentPlayerIndex = getPlayerRolePosition(bigBlindIndex)
        endRoundIndex = currentPlayerIndex

        potAmount = 0
        currentHighBet = 0

        Log.d("GAME", "Small blind index: $smallBlindIndex")
        Log.d("GAME", "Big blind index: $bigBlindIndex")
        Log.d("GAME", "Current player index: $currentPlayerIndex")

        updatePot(
            players[smallBlindIndex]
                .paySmallBlind(smallBlind)
        )

        updatePot(
            players[bigBlindIndex]
                .payBigBlind(bigBlind)
        )
        currentHighBet = bigBlind

    }

    fun nextRoundInit(round: GameRound): GameRound {

        if(players.size < 2){
            //TODO: Handle Exception -> print message and redirect to home screen
            throw Exception("Not enough players")
        }

        if(showdownEdgeCases()){
            Log.d("GAME", "Moving to showdown round. No more player actions available.")
            return GameRound.SHOWDOWN
        }

        currentPlayerIndex = getPlayerRolePosition(currentPlayerIndex)
        Log.d("GAME", "Current player index: $currentPlayerIndex")

        if(currentPlayerIndex != -1 && !checkPlayerHighBet(currentPlayerIndex)){
            Log.d("GAME", "Current round is not finished yet.")
            return round
        }

        Log.d("GAME", "Initializing next round...")

        raiseFlag = false
        currentPlayerIndex = -1
        endRoundIndex = -1

        var countPlayersWithActions = 0
        players.forEach {
                player ->
                    player.playerBet = 0
                    if(player.playerState != PlayerState.FOLD
                        && player.playerState != PlayerState.ALL_IN
                        && player.playerState != PlayerState.SPECTATOR
                    ){
                        player.playerState = PlayerState.INACTIVE
                        countPlayersWithActions++
                    }
        }

        if(countPlayersWithActions <= 1){
            Log.d("GAME", "Moving to showdown round. No more player actions available.")
            return GameRound.SHOWDOWN
        }

        Log.d("GAME", "Updating current player index...")

        currentPlayerIndex = getPlayerRolePosition(dealerButtonPos)

        Log.d("GAME", "Current player index: $currentPlayerIndex")

        endRoundIndex = currentPlayerIndex

        currentHighBet = 0

        Log.d("GAME", "Next round: ${round.nextRound().name}")

        return round.nextRound()
    }

    private fun showdownEdgeCases(): Boolean {
        Log.d("GAME", "Checking for showdown round edge cases...")
        val countFolds = players.count {
            it.playerState == PlayerState.FOLD
                    || it.playerState == PlayerState.SPECTATOR
        }

        if(countFolds == players.size - 1){
            return true
        }

        return players.count {
            it.playerState == PlayerState.FOLD
                    || it.playerState == PlayerState.ALL_IN
                    || it.playerState == PlayerState.SPECTATOR
        } == players.size

    }

    private fun checkEndRoundIndex(playerIndex: Int): Boolean {
        Log.d("GAME", "Checking end round index.")
        Log.d("GAME", "Player index: $playerIndex; End round index: $endRoundIndex")
        return playerIndex == endRoundIndex && !raiseFlag
    }

    private fun checkPlayerHighBet(playerIndex: Int): Boolean {
        Log.d("GAME", "Checking player high bet.")
        Log.d("GAME", "Player bet: ${players[playerIndex].playerBet}; High bet: $currentHighBet")
        return players[playerIndex].playerBet == currentHighBet && raiseFlag
    }

    private fun getPlayerRolePosition(playerRoleStart: Int): Int {
        var playerRoleIndex = playerRoleStart
        do {
            playerRoleIndex = (playerRoleIndex + 1) % players.size
            if(checkEndRoundIndex(playerRoleIndex)){
                return -1
            }
        }while(
            players[playerRoleIndex].playerState == PlayerState.FOLD
            || players[playerRoleIndex].playerState == PlayerState.ALL_IN
            || players[playerRoleIndex].playerState == PlayerState.SPECTATOR
        )
        return playerRoleIndex
    }

    private fun shuffleCardsDeck(): List<PlayingCard> {
        return PlayingCard.entries.shuffled()
    }

    private fun generateCommunityCards(cards: List<PlayingCard>) {
        communityCards = cards.drop(CardConstants.HOLE_CARDS * players.size)
            .take(CardConstants.COMMUNITY_CARDS).toMutableList()
    }

    private fun generateHoleCards(cards: List<PlayingCard>) {
        val generatedHoleCards = cards.take(CardConstants.HOLE_CARDS * players.size)

        for((index, player) in players.withIndex()){
            player.assignHoleCards(
                Pair(generatedHoleCards[index*2], generatedHoleCards[index*2 + 1])
            )
        }
    }

    fun showStreet(gameRound: GameRound): MutableList<PlayingCard> {
        return when(gameRound){
            GameRound.PREFLOP -> mutableListOf()
            GameRound.FLOP -> communityCards
                .subList(CardConstants.FLOP_CARDS_START, CardConstants.FLOP_CARDS_END)
            GameRound.TURN -> communityCards
                .subList(CardConstants.FLOP_CARDS_START, CardConstants.TURN_CARD_INDEX)
            GameRound.RIVER -> communityCards
                .subList(CardConstants.FLOP_CARDS_START, CardConstants.RIVER_CARD_INDEX)
            else -> communityCards
        }
    }

    fun updatePot(playerBet: Int) {
        potAmount += playerBet
    }

    private fun combinationUtil(
        comCards: MutableList<PlayingCard>, tmpCardComb: Array<PlayingCard?>, start: Int,
        end: Int, index: Int, r: Int, handEvaluator: CardHandEvaluator,
        player: Player, cardCombinations: MutableList<PlayingCard>
    ) {

        if (index == r) {
            for (j in 0 until r) {
                tmpCardComb[j]?.let { cardCombinations.add(it) }
            }
            cardCombinations.add(player.getHoleCards().first)
            cardCombinations.add(player.getHoleCards().second)

            val combinationHandRank = handEvaluator.getHandRanking(cardCombinations)
            if(combinationHandRank.second < player.playerHandRank.second){
                player.playerHandRank = combinationHandRank
            }
            cardCombinations.clear()
            return
        }

        var i = start
        while (i <= end && end - i + 1 >= r - index) {
            tmpCardComb[index] = comCards[i]
            combinationUtil(
                comCards, tmpCardComb, i + 1, end,
                index + 1, r, handEvaluator, player, cardCombinations
            )
            i++
        }
    }

    fun rankCardHands(): MutableList<Player> {
        val tmpCardComb = arrayOfNulls<PlayingCard>(CardConstants.HAND_COMBINATION)
        val cardCombinations = mutableListOf<PlayingCard>()
        val handEvaluator = CardHandEvaluator()
        val winner: MutableList<Player> = mutableListOf(players[0])
        players.forEach {
            player ->
                if(player.playerState != PlayerState.FOLD
                    && player.playerState != PlayerState.SPECTATOR) {
                    combinationUtil(
                        communityCards, tmpCardComb, 0,
                        CardConstants.COMMUNITY_CARDS - 1, 0,
                        CardConstants.HAND_COMBINATION, handEvaluator,
                        player, cardCombinations
                    )
                }

                if(player.playerHandRank.second < winner[0].playerHandRank.second){
                    winner.clear()
                    winner.add(player)
                }
                else if(player.playerHandRank.second == winner[0].playerHandRank.second){
                    winner.add(player)
                }
        }

        return winner
    }

    fun assignChipsToWinner(winners: MutableList<Player>) {
        val splitPot = potAmount / winners.size
        val leftoverChips = potAmount % winners.size
        winners.forEach {
            player ->
                player.playerState = PlayerState.WINNER
                player.assignChips(splitPot)
        }

        if(leftoverChips > 0){
            players[getPlayerRolePosition(dealerButtonPos)].assignChips(leftoverChips)
        }
    }

    override fun toString(): String {
        return "Game(potAmount=$potAmount, currentHighBet=$currentHighBet)"
    }
}
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
                endRoundIndex = (endRoundIndex + 1) % players.size
            }
            if(players.indexOf(player) == currentPlayerIndex){
                iterateCurrentPlayerIndex()
            }
            players.remove(player)
            println("Player ${player.user.username} has left the game.")
        }
    }

    fun preflopRoundInit() {
        raiseFlag = false

        Log.d("GAME", "Initializing preflop round...")

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

        updateDealerButtonPosition()

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

        iterateCurrentPlayerIndex()

        if(!isCurrentRoundFinished()){
            Log.d("GAME", "Current round is not finished yet.")
            return round
        }

        Log.d("GAME", "Initializing next round...")

        raiseFlag = false

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

    private fun isCurrentRoundFinished(): Boolean {
        return !((currentPlayerIndex != endRoundIndex && !raiseFlag) ||
                (players[currentPlayerIndex].playerBet != currentHighBet))
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

    private fun iterateCurrentPlayerIndex(){
        Log.d("GAME",
            "Iterating current player index. Current player index: $currentPlayerIndex")
        do{
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size
        }while(
            players[currentPlayerIndex].playerState == PlayerState.FOLD
            || players[currentPlayerIndex].playerState == PlayerState.ALL_IN
            || players[currentPlayerIndex].playerState == PlayerState.SPECTATOR
        )
        Log.d("GAME",
            "Next player index: $currentPlayerIndex")
    }

    private fun updateDealerButtonPosition() {
        Log.d("GAME",
            "Updating dealer button position. Current dealer button index: $dealerButtonPos")
        do {
            dealerButtonPos = (dealerButtonPos + 1) % players.size
        }while(
            players[dealerButtonPos].playerState == PlayerState.SPECTATOR
        )
        Log.d("GAME", "Next dealer button index: $dealerButtonPos")
    }

    private fun getPlayerRolePosition(playerRoleOffset: Int): Int {
        var playerIndex = playerRoleOffset
        do {
            playerIndex = (playerIndex + 1) % players.size
        }while(
            players[playerIndex].playerState == PlayerState.FOLD
                || players[playerIndex].playerState == PlayerState.ALL_IN
                || players[playerIndex].playerState == PlayerState.SPECTATOR
        )
        return playerIndex
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
        if(winners.size == 1){
            winners[0].assignChips(potAmount)
        }
        else{
            val splitPot = potAmount / 2
            winners[0].assignChips(splitPot)
            winners[1].assignChips(splitPot)
        }
    }

    fun gameRoundSim() {
        var raiseFlag = false

        do{
            println("Select: call, raise, check, fold")
            var playerAction = readLine()!!

            when(playerAction){
                "call" -> {
                    updatePot(players[currentPlayerIndex].call(currentHighBet))
                }
                "raise" -> {
                    println("Enter raise amount: ")
                    var raiseAmount = readLine()!!

                    updatePot(players[currentPlayerIndex]
                        .raise(currentHighBet, raiseAmount.toInt())
                    )
                    currentHighBet = players[currentPlayerIndex].playerBet
                    raiseFlag = true
                }
                "check" -> players[currentPlayerIndex].check()
                "fold" -> players[currentPlayerIndex].fold()
            }

            iterateCurrentPlayerIndex()

            println(toString())
            players.forEach {
                player ->  println(player.toString())
            }

        }while((currentPlayerIndex != endRoundIndex && !raiseFlag) ||
            (players[currentPlayerIndex].playerBet != currentHighBet)
        )

    }

    override fun toString(): String {
        return "Game(potAmount=$potAmount, currentHighBet=$currentHighBet)"
    }
}
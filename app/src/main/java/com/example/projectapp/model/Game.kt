package com.example.projectapp.model

import com.example.projectapp.data.GameRound
import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard

class Game(): TableActions {

    var players: MutableList<Player> = mutableListOf()
    var potAmount: Int = 0
    var currentHighBet: Int = 0
    val smallBlind: Int = 25
    val bigBlind: Int = 50
    private var communityCards: MutableList<PlayingCard> = mutableListOf()
    private var cardCombinations: MutableList<PlayingCard> = mutableListOf()
    var dealerButtonPos: Int = -1
    var currentPlayerIndex: Int = -1
    var endRoundIndex: Int = -1

    object CardConstants {
        const val COMMUNITY_CARDS = 5
        const val HOLE_CARDS = 2
        const val FLOP_CARDS_START = 0
        const val FLOP_CARDS_END = 3
        const val TURN_CARD_INDEX = 3
        const val RIVER_CARD_INDEX = 4
        const val HAND_COMBINATION = 3
    }

    object PlayerRoleOffsets {
        const val SMALL_BLIND = 1
        const val BIG_BLIND = 2
        const val UNDER_THE_GUN = 3
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
            players.remove(player)
            println("Player ${player.user.username} has left the game.")
        }
    }

    fun preflopRoundInit() {
        players.forEach {
            player ->
                player.playerState = PlayerState.NONE
                player.playerHandRank = Pair(HandRankings.HIGH_CARD, 7462)
                player.playerBet = 0
        }

        updateDealerButtonPos()

        val cards: List<PlayingCard> = shuffleCardsDeck()
        generateHoleCards(cards)
        generateCommunityCards(cards)

        currentPlayerIndex = getPlayerRolePos(PlayerRoleOffsets.UNDER_THE_GUN)
        endRoundIndex = getPlayerRolePos(PlayerRoleOffsets.BIG_BLIND)
        potAmount = 0
        currentHighBet = 0

        updatePot(
            players[getPlayerRolePos(PlayerRoleOffsets.SMALL_BLIND)]
                .paySmallBlind(smallBlind)
        )
        updatePot(
            players[getPlayerRolePos(PlayerRoleOffsets.BIG_BLIND)]
                .payBigBlind(bigBlind)
        )
        currentHighBet = bigBlind

    }

    fun streetRoundInit(){
        currentPlayerIndex = getPlayerRolePos(PlayerRoleOffsets.SMALL_BLIND)

        while(players[currentPlayerIndex].playerState == PlayerState.FOLD){
            iterateCurrentPlayerIndex()
        }
        endRoundIndex = currentPlayerIndex
    }

    private fun iterateCurrentPlayerIndex(){
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
    }

    private fun shuffleCardsDeck(): List<PlayingCard> {
        return PlayingCard.entries.shuffled()
    }

    override fun generateCommunityCards(cards: List<PlayingCard>) {
        communityCards = cards.drop(CardConstants.HOLE_CARDS * players.size)
            .take(CardConstants.COMMUNITY_CARDS).toMutableList()
    }

    override fun generateHoleCards(cards: List<PlayingCard>) {
        val generatedHoleCards = cards.take(CardConstants.HOLE_CARDS * players.size)

        for((index, player) in players.withIndex()){
            player.assignHoleCards(
                Pair(generatedHoleCards[index*2], generatedHoleCards[index*2 + 1])
            )
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

    private fun combinationUtil(
        comCards: MutableList<PlayingCard>, tmpCardComb: Array<PlayingCard?>, start: Int,
        end: Int, index: Int, r: Int, handEvaluator: CardHandEvaluator, player: Player
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
                index + 1, r, handEvaluator, player
            )
            i++
        }
    }

    fun rankCardHands(): MutableList<Player> {
        val tmpCardComb = arrayOfNulls<PlayingCard>(CardConstants.HAND_COMBINATION)
        val handEvaluator = CardHandEvaluator()
        val winner: MutableList<Player> = mutableListOf(players[0])
        players.forEach {
            player ->
                if(player.playerState != PlayerState.FOLD) {
                    combinationUtil(
                        communityCards, tmpCardComb, 0,
                        CardConstants.COMMUNITY_CARDS - 1, 0,
                        CardConstants.HAND_COMBINATION, handEvaluator, player
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

    override fun toString(): String {
        return "Game(potAmount=$potAmount, currentHighBet=$currentHighBet)"
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

            do{
                iterateCurrentPlayerIndex()
            }while(players[currentPlayerIndex].playerState == PlayerState.FOLD)


            println(toString())
            players.forEach {
                player ->  println(player.toString())
            }

        }while((currentPlayerIndex != endRoundIndex && !raiseFlag) ||
            (players[currentPlayerIndex].playerBet != currentHighBet)
        )

    }
}
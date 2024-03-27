package com.example.projectapp.model

import com.example.projectapp.data.GameRound
import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard
import com.example.projectapp.data.Tables

class Game(): TableActions {

    var players: MutableList<Player> = mutableListOf()
    var potAmount: Int = 0
    var currentHighBet: Int = 0
    val smallBlind: Int = 25
    val bigBlind: Int = 50
    private var communityCards: MutableList<PlayingCard> = mutableListOf()
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
                player.playerBet = 0
        }

        updateDealerButtonPos()

        generateHoleCards()
        generateCommunityCards()

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

    override fun generateCommunityCards() {
        communityCards = PlayingCard.entries.shuffled()
            .take(CardConstants.COMMUNITY_CARDS).toMutableList()
    }

    override fun generateHoleCards() {
        val generatedHoleCards = PlayingCard.entries
            .shuffled().take(CardConstants.HOLE_CARDS * players.size)

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

    fun rankCardHands(cards: MutableList<PlayingCard>): HandRankings {
        //TODO("Loop through players and compare highest hand of each player")
        val handEvaluator = CardHandEvaluator()
        handEvaluator.setCards(cards)

        return handEvaluator.getHandRanking()
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
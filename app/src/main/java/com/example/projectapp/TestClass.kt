package com.example.projectapp

import com.example.projectapp.data.GameRound
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard
import com.example.projectapp.model.Game
import com.example.projectapp.model.Player
import com.example.projectapp.model.User


fun gameRoundSim(game: Game, playerIndex: Int){

    var raiseFlag: Boolean = false
    var playerIndex: Int = playerIndex

    do{

        if(game.players[playerIndex].playerState == PlayerState.FOLD) {
            continue
        }

        println("Select: call, raise, check, fold")
        var playerAction = readLine()!!

        when(playerAction){
            "call" -> {
                game.updatePot(game.players[playerIndex].call(game.currentHighBet))
                game.players[playerIndex].playerState = PlayerState.CALL
            }
            "raise" -> {
                println("Enter raise amount: ")
                var raiseAmount = readLine()!!

                game.updatePot(game.players[playerIndex]
                    .raise(game.currentHighBet, raiseAmount.toInt())
                )
                game.currentHighBet = game.players[playerIndex].playerBet
                game.players[playerIndex].playerState = PlayerState.RAISE
                raiseFlag = true
            }
            "check" -> game.players[playerIndex].playerState = PlayerState.CHECK
            "fold" -> game.players[playerIndex].playerState = PlayerState.FOLD
        }

        playerIndex = (playerIndex + 1) % game.players.size

        println(game.toString())
        println(game.players[0].toString())
        println(game.players[1].toString())
        println(game.players[2].toString())
        println(playerIndex)

    }while((playerIndex != game.dealerButtonPos && !raiseFlag) ||
        game.players[playerIndex].playerBet != game.currentHighBet)

}

fun main() {
    //Create user accounts
    val user1 = User("User1")
    val user2 = User("User2")
    val user3 = User("User3")
    //Init players for the game
    val player1 = Player(user1, chipAmount = 500)
    val player2 = Player(user2, chipAmount = 900)
    val player3 = Player(user3, chipAmount = 1000)
    val listOfPlayers: MutableList<Player> = mutableListOf()

    listOfPlayers.add(player1)
    listOfPlayers.add(player2)
    listOfPlayers.add(player3)

    //Init the game and add players
    val game = Game(listOfPlayers)

    for(round in GameRound.entries){

        var playerIndex: Int
        var raiseFlag: Boolean = false

        if(round == GameRound.PREFLOP) {
            game.generateCommunityCards()
            game.generateHoleCards()

            //Move dealar button position at beginning of each game
            game.updateDealerButtonPos()

            //Players bet the smallBlind and bigBlind values. Pot is updated.
            game.updatePot(
                game.players[(game.dealerButtonPos + 1) % game.players.size]
                    .paySmallBlind(game.smallBlind)
            )
            game.updatePot(
                game.players[(game.dealerButtonPos + 2) % game.players.size]
                    .payBigBlind(game.bigBlind)
            )
            game.currentHighBet = game.bigBlind

            playerIndex = (game.dealerButtonPos + 3) % game.players.size

            gameRoundSim(game, playerIndex)

        }
        else if(round == GameRound.FLOP){

            println(game.showFlop())

            playerIndex = (game.dealerButtonPos + 1) % game.players.size

            gameRoundSim(game, playerIndex)
        }
    }

}

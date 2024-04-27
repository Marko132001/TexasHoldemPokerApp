package com.example.projectapp

import com.example.projectapp.data.GameRound
import com.example.projectapp.data.PlayerState
import com.example.projectapp.model.Game
import com.example.projectapp.model.Player
import com.example.projectapp.model.User


fun main() {

    val user1 = User("User1")
    val user2 = User("User2")
    val user3 = User("User3")

    val player1 = Player(user1, chipBuyInAmount = 500)
    val player2 = Player(user2, chipBuyInAmount = 900)
    val player3 = Player(user3, chipBuyInAmount = 1000)

    val game = Game()

    game.playerJoin(player1)
    game.playerJoin(player2)
    game.playerJoin(player3)


    while(game.players.size >= 2) {
        game.preflopRoundInit()

        println("Remove player? Y/N")
        var removePlayer = readLine()!!
        if(removePlayer == "Y"){
            game.playerQuit(game.players[game.players.size - 1])
        }

        for (round in GameRound.entries) {

            when (game.nextRoundInit(round)) {
                GameRound.PREFLOP -> {
                    game.gameRoundSim()
                }

                GameRound.SHOWDOWN -> {
                    game.assignChipsToWinner(game.rankCardHands())

                    println("Community cards: " + game.showStreet(GameRound.SHOWDOWN))
                    for (player in game.players) {
                        if (player.playerState != PlayerState.FOLD) {
                            println(player.user.username + ": " + player.getHoleCards() + " " + player.playerHandRank)
                        }
                    }

                    break
                }

                else -> {
                    println(game.showStreet(round))
                    //game.streetRoundInit()
                    game.gameRoundSim()
                }
            }
        }

    }

}

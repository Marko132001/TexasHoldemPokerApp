package com.example.projectapp

import com.example.projectapp.data.GameRound
import com.example.projectapp.data.PlayerState
import com.example.projectapp.data.PlayingCard
import com.example.projectapp.model.Game
import com.example.projectapp.model.Player
import com.example.projectapp.model.User


fun main() {
    //Create user accounts
    val user1 = User("User1")
    val user2 = User("User2")
    val user3 = User("User3")
    //Init players for the game
    val player1 = Player(user1, chipBuyInAmount = 500)
    val player2 = Player(user2, chipBuyInAmount = 900)
    val player3 = Player(user3, chipBuyInAmount = 1000)
    val listOfPlayers: MutableList<Player> = mutableListOf()

    listOfPlayers.add(player1)
    listOfPlayers.add(player2)
    listOfPlayers.add(player3)

    //Init the game and add players
    val game = Game(listOfPlayers)


    for(round in GameRound.entries){
        var round: GameRound = round

        if(game.players.count{it.playerState == PlayerState.FOLD} == game.players.size - 1){
            round = GameRound.SHOWDOWN
        }

        when(round) {
            GameRound.PREFLOP -> {
                game.preflopRoundInit()
                game.gameRoundSim()
            }
            GameRound.SHOWDOWN -> {
                println("Community cards: " + game.showStreet(GameRound.SHOWDOWN))
                for(player in game.players){
                    if(player.playerState != PlayerState.FOLD){
                        println(player.user.username + ": " + player.getHoleCards())
                    }
                }
                //TODO "Rank Hands and select a winner. Reassign chips between players"
                break
            }
            else -> {
                println(game.showStreet(round))
                game.streetRoundInit()
                game.gameRoundSim()
            }
        }
    }

}

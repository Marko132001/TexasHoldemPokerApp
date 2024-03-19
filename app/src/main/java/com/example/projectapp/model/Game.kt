package com.example.projectapp.model

import com.example.projectapp.data.PlayingCard

class Game(var players: MutableList<Player>) {
    var potAmount: Int = 0
    var currentBet: Int = 0
    //communityCards --> Preflop(0), Flop(3), Turn(1), River(1)
    var communityCards: MutableList<PlayingCard> = mutableListOf<PlayingCard>()
    var dealerButtonPos: Int = 1
}
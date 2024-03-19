package com.example.projectapp.model

interface PlayerRoundActions {
    fun call(currentBet: Int): Int
    fun raise(currentBet: Int, raiseAmount: Int): Int
    fun paySmallBlind(smallBlindValue: Int): Int
    fun payBigBlind(bigBlindValue: Int): Int
    fun check()
    fun fold()
}
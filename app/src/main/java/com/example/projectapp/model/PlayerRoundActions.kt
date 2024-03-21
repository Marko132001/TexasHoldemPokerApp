package com.example.projectapp.model

interface PlayerRoundActions {
    fun call(currentHighBet: Int): Int
    fun raise(currentHighBet: Int, raiseAmount: Int): Int
    fun paySmallBlind(smallBlindValue: Int): Int
    fun payBigBlind(bigBlindValue: Int): Int
    fun check()
    fun fold()
}
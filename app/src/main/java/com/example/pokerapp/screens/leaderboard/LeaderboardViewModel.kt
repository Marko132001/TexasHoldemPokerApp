package com.example.pokerapp.screens.leaderboard

import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.navigation.HOME_SCREEN
import com.example.pokerapp.navigation.LEADERBOARD_SCREEN
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {

    val users = accountService.users

    fun onBackButtonClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(HOME_SCREEN, LEADERBOARD_SCREEN)
    }
}
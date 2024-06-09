package com.example.pokerapp.screens.home

import android.util.Log
import com.example.pokerapp.navigation.GAME_SCREEN
import com.example.pokerapp.navigation.HOME_SCREEN
import com.example.pokerapp.navigation.LOGIN_SCREEN
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.navigation.LEADERBOARD_SCREEN
import com.example.pokerapp.navigation.SETTINGS_SCREEN
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {

    val minBuyIn = 1000
    val maxBuyIn = 5000

    init {
        launchCatching {
            accountService.currentUser.collect {
                _userData.value = it
            }
        }
    }

    fun onPlayClick(openAndPopUp: (String, String) -> Unit, buyInValue: Int) {
        val userId = accountService.currentUserId
        Log.d("HOMESCREEN", "Client user id: $userId, Buy-in amount: $buyInValue")
        openAndPopUp("$GAME_SCREEN/$buyInValue", HOME_SCREEN)
    }

    fun onLeaderboardsClick(openScreen: (String) -> Unit){
        openScreen(LEADERBOARD_SCREEN)
    }

    fun onSettingsClick(openAndPopUp: (String, String) -> Unit){
        openAndPopUp(SETTINGS_SCREEN, HOME_SCREEN)
    }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(LOGIN_SCREEN)
        }
    }

}
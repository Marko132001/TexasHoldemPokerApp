package com.example.pokerapp.screens.home

import android.util.Log
import com.example.pokerapp.GAME_SCREEN
import com.example.pokerapp.HOME_SCREEN
import com.example.pokerapp.LOGIN_SCREEN
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {

    fun onPlayClick(openAndPopUp: (String, String) -> Unit) {
        val userId = accountService.currentUserId
        Log.d("HOMESCREEN", "Client user id: $userId")
        launchCatching {
            openAndPopUp(GAME_SCREEN, HOME_SCREEN)
        }
        //TODO: Redirect to GameScreen and send userId to server
    }

    fun onLeaderboardsClick(openScreen: (String) -> Unit){
        //openScreen(LEADERBOARDS_SCREEN)
        //TODO: Redirect to leaderboards screen (don't pop from backstack)
    }

    fun onSettingsClick(openScreen: (String) -> Unit){
        //openScreen(SETTINGS_SCREEN)
        //TODO: Redirect to settings screen (don't pop from backstack)
    }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        launchCatching {
            accountService.signOut()
            restartApp(LOGIN_SCREEN)
        }
    }

}
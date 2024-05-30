package com.example.pokerapp.screens.home

import android.util.Log
import com.example.pokerapp.model.UserData
import com.example.pokerapp.navigation.GAME_SCREEN
import com.example.pokerapp.navigation.HOME_SCREEN
import com.example.pokerapp.navigation.LOGIN_SCREEN
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {

    val minBuyIn = 1000
    val maxBuyIn = 5000

    private val _userData = MutableStateFlow(UserData())
    val userData = _userData.asStateFlow()

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
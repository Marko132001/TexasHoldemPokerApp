package com.example.pokerapp.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokerapp.GAME_SCREEN
import com.example.pokerapp.GameActivity
import com.example.pokerapp.HOME_SCREEN
import com.example.pokerapp.LOGIN_SCREEN
import com.example.pokerapp.PokerAppState
import com.example.pokerapp.SIGN_UP_SCREEN
import com.example.pokerapp.screens.home.HomeScreen
import com.example.pokerapp.screens.login.LoginScreen
import com.example.pokerapp.screens.sign_up.SignupScreen
import com.example.pokerapp.ui.GameViewModel
import com.example.pokerapp.ui.PokerGame
import kotlinx.coroutines.CoroutineScope

@Composable
fun Navigation() {
    val appState = rememberAppState()

    NavHost(
        navController = appState.navController,
        startDestination = LOGIN_SCREEN
    ) {
        composable(route = LOGIN_SCREEN) {
            LoginScreen(openAndPopUp = {
                route, popUp -> appState.navigateAndPopUp(route, popUp)
            })
        }
        composable(route = SIGN_UP_SCREEN) {
            SignupScreen(openAndPopUp = {
                route, popUp -> appState.navigateAndPopUp(route, popUp)
            })
        }
        composable(route = HOME_SCREEN) {
            HomeScreen(
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                openScreen = { route -> appState.navigate(route) },
                restartApp = { route -> appState.clearAndNavigate(route) }
            )
        }
        composable(route = GAME_SCREEN) {
            val gameViewModel: GameViewModel = hiltViewModel<GameViewModel>()

            val context = LocalContext.current
            val activity = context as Activity
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

            val gameUiState by gameViewModel.state.collectAsStateWithLifecycle()

            val isConnecting by gameViewModel.isConnecting.collectAsStateWithLifecycle()
            val showConnectionError by gameViewModel.showConnectionError.collectAsStateWithLifecycle()

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                PokerGame(
                    context,
                    gameViewModel,
                    gameUiState,
                    isConnecting,
                    showConnectionError
                )
            }
        }
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(navController, coroutineScope) {
        PokerAppState(navController, coroutineScope)
    }
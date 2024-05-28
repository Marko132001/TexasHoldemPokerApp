package com.example.pokerapp.navigation

import android.app.Activity
import android.content.pm.ActivityInfo
import android.util.Log
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokerapp.screens.home.HomeScreen
import com.example.pokerapp.screens.login.LoginScreen
import com.example.pokerapp.screens.sign_up.SignupScreen
import com.example.pokerapp.screens.game.GameViewModel
import com.example.pokerapp.screens.game.PokerGame
import com.example.pokerapp.screens.home.HomeScreenViewModel
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
        composable(
            route = "$GAME_SCREEN/{buyInValue}",
            arguments = listOf(
                navArgument("buyInValue"){
                    type = NavType.IntType
                }
            )
        ) {

            val buyInValue = it.arguments?.getInt("buyInValue") ?: 0
            Log.d("GAMESCREEN", "Buy-in value: $buyInValue")

            val gameViewModel: GameViewModel = hiltViewModel(
                creationCallback = {factory: GameViewModel.DetailViewModelFactory ->
                    factory.create(buyInValue)
                }
            )

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
package com.example.pokerapp.navigation

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope

@Composable
fun Navigation() {
    val appState = rememberAppState()
    val context = LocalContext.current
    val activity = context as Activity

    val view = LocalView.current
    val window = (view.context as Activity).window

    val insetsController = WindowCompat.getInsetsController(window, view)

    NavHost(
        navController = appState.navController,
        startDestination = if(FirebaseAuth.getInstance().currentUser == null) LOGIN_SCREEN else HOME_SCREEN
    ) {
        composable(route = LOGIN_SCREEN) {
            window.statusBarColor = Color.White.toArgb()
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            LoginScreen(
                context = context,
                openAndPopUp = {
                    route, popUp -> appState.navigateAndPopUp(route, popUp)
                }
            )
        }
        composable(route = SIGN_UP_SCREEN) {
            window.statusBarColor = Color.White.toArgb()
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            SignupScreen(
                context = context,
                openAndPopUp = {
                    route, popUp -> appState.navigateAndPopUp(route, popUp)
                }
            )
        }
        composable(route = HOME_SCREEN) {
            window.statusBarColor = Color(0xff1893b5).toArgb()
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            HomeScreen(
                openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                openScreen = { route -> appState.navigate(route) },
                restartApp = { route -> appState.clearAndNavigate(route) }
            )
        }
        composable(
            route = GAME_SCREEN_PARAMS,
            arguments = listOf(
                navArgument("buyInValue"){
                    type = NavType.IntType
                }
            )
        ) {

            val buyInValue = it.arguments?.getInt("buyInValue") ?: 0

            val gameViewModel: GameViewModel = hiltViewModel(
                creationCallback = {factory: GameViewModel.DetailViewModelFactory ->
                    factory.create(buyInValue)
                }
            )

            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            insetsController.apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

            val gameUiState by gameViewModel.state.collectAsStateWithLifecycle()

            val isConnecting by gameViewModel.isConnecting.collectAsStateWithLifecycle()
            val showConnectionError by gameViewModel.showConnectionError.collectAsStateWithLifecycle()

            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                PokerGame(
                    context = context,
                    openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) },
                    gameViewModel = gameViewModel,
                    gameUiState = gameUiState,
                    isConnecting = isConnecting,
                    showConnectionError = showConnectionError
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
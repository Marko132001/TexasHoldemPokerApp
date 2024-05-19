package com.example.pokerapp.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pokerapp.LOGIN_SCREEN
import com.example.pokerapp.PokerAppState
import com.example.pokerapp.SIGN_UP_SCREEN
import com.example.pokerapp.screens.login.LoginScreen
import com.example.pokerapp.screens.sign_up.SignupScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun Navigation() {
    val appState = rememberAppState()

    NavHost(
        navController = appState.navController,
        startDestination = SIGN_UP_SCREEN
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
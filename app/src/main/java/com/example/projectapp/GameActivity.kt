package com.example.projectapp

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.projectapp.ui.GameViewModel
import com.example.projectapp.ui.PokerGame
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                PokerGame(context, gameViewModel, gameUiState, isConnecting, showConnectionError)
            }
        }
    }
}
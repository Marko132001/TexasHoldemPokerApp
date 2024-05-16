package com.example.projectapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.projectapp.data.PlayerState
import com.example.projectapp.model.GameState

@Composable
fun ActionButtons(
    actionButtons: Modifier,
    gameViewModel: GameViewModel,
    gameUiState: GameState
) {

    Row(
        modifier = actionButtons,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Button(
            onClick = {
                if(gameViewModel.timerProgress > 0.01f) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.FOLD.name, -1)
                }
            },
            enabled = gameUiState.isFoldEnabled,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            )
        ) {
            Text(text = "FOLD")
        }
        Button(
            onClick = {
                if(gameViewModel.timerProgress > 0.01f) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.CHECK.name, -1)
                }
            },
            enabled = gameUiState.isCheckEnabled,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            )
        ) {
            Text(text = "CHECK")
        }
        Button(
            onClick = {
                if(gameViewModel.timerProgress > 0.01f) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.CALL.name, -1)
                }
            },
            enabled = gameUiState.isCallEnabled,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            )
        ) {
            Text(text = "CALL")
        }
        if(!gameViewModel.isRaiseSlider) {
            Button(
                onClick = {
                    if(gameViewModel.timerProgress > 0.01f) {
                        gameViewModel.isRaiseSlider = true
                    }
                },
                enabled = gameUiState.isRaiseEnabled,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                ),
                contentPadding = PaddingValues(horizontal = 37.dp)
            ) {
                Text(text = "RAISE")
            }
        }
        else{
            Button(
                onClick = {
                    if(gameViewModel.timerProgress > 0.01f) {
                        gameViewModel.isRaiseSlider = false
                        gameViewModel.playerAction(
                            PlayerState.RAISE.name,
                            gameViewModel.raiseAmount
                        )
                    }
                },
                enabled = gameUiState.isRaiseEnabled,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(text = "CONFIRM")
            }
        }
    }
}

@Composable
fun RaiseAmountSlider(
    minimumRaise: Float,
    maximumRaise: Float,
    gameViewModel: GameViewModel
) {
    Column(
        modifier = Modifier
            .wrapContentSize(Alignment.CenterEnd)
            .padding(bottom = 37.dp),
    ) {
        SliderWithLabel(
            finiteEnd = true,
            valueRange = minimumRaise..maximumRaise,
            gameViewModel = gameViewModel
        )
    }
}
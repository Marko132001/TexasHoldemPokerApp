package com.example.pokerapp.ui.components.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pokerapp.data.PlayerState
import com.example.pokerapp.model.GameState
import com.example.pokerapp.screens.game.GameViewModel

@Composable
fun ActionButtons(
    actionButtonsModifier: Modifier,
    gameViewModel: GameViewModel,
    gameUiState: GameState,
    clientActionTurn: Boolean
) {

    Row(
        modifier = actionButtonsModifier
            .padding(bottom = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ){
        ActionButton(
            buttonText = "FOLD",
            buttonEnabled = gameUiState.isFoldEnabled,
            onButtonClick = {
                if(clientActionTurn) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.FOLD.name, -1)
                }
            }
        )
        ActionButton(
            buttonText = "CHECK",
            buttonEnabled = gameUiState.isCheckEnabled,
            onButtonClick = {
                if(clientActionTurn) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.CHECK.name, -1)
                }
            }
        )
        ActionButton(
            buttonText = "CALL",
            buttonEnabled = gameUiState.isCallEnabled,
            onButtonClick = {
                if(clientActionTurn) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.CALL.name, -1)
                }
            }
        )

        if(!gameViewModel.isRaiseSlider) {
            ActionButton(
                buttonText = "RAISE",
                buttonEnabled = gameUiState.isRaiseEnabled,
                onButtonClick = {
                    if(clientActionTurn) {
                        gameViewModel.isRaiseSlider = true
                    }
                }
            )
        }
        else{
            ActionButton(
                buttonText = "CONFIRM",
                buttonEnabled = gameUiState.isRaiseEnabled,
                onButtonClick = {
                    if(clientActionTurn) {
                        gameViewModel.isRaiseSlider = false
                        gameViewModel.playerAction(
                            PlayerState.RAISE.name,
                            gameViewModel.raiseAmount
                        )
                    }
                }
            )
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
package com.example.pokerapp.ui.components.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokerapp.data.PlayerState
import com.example.pokerapp.model.GameState
import com.example.pokerapp.screens.game.GameViewModel
import kotlin.math.roundToInt

@Composable
fun ActionButtons(
    actionButtonsModifier: Modifier,
    gameViewModel: GameViewModel,
    gameUiState: GameState,
    clientActionTurn: Boolean,
    minimumRaise: Float,
    maximumRaise: Float
) {

    val valueRange = minimumRaise..maximumRaise
    var steps = (valueRange.endInclusive / valueRange.start).toInt() - 2
    if(steps < 0){
        steps = 0
    }

    var sliderPosition by remember { mutableFloatStateOf(valueRange.start) }
    gameViewModel.raiseAmount = sliderPosition.toInt()


    Row(
        modifier = actionButtonsModifier
            .padding(bottom = 10.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        ActionButton(
            buttonText = "FOLD",
            buttonEnabled = gameUiState.isFoldEnabled,
            onButtonClick = {
                if(clientActionTurn && gameViewModel.timerProgress > 0.05) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.FOLD.name, -1)
                }
            }
        )
        ActionButton(
            buttonText = "CHECK",
            buttonEnabled = gameUiState.isCheckEnabled,
            onButtonClick = {
                if(clientActionTurn && gameViewModel.timerProgress > 0.05) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.CHECK.name, -1)
                }
            }
        )
        ActionButton(
            buttonText = "CALL",
            buttonEnabled = gameUiState.isCallEnabled,
            onButtonClick = {
                if(clientActionTurn && gameViewModel.timerProgress > 0.05) {
                    gameViewModel.isRaiseSlider = false
                    gameViewModel.playerAction(PlayerState.CALL.name, -1)
                }
            }
        )

        if(!gameViewModel.isRaiseSlider) {
            sliderPosition = valueRange.start
            ActionButton(
                buttonText = "RAISE",
                buttonEnabled = gameUiState.isRaiseEnabled &&
                        gameUiState.players[gameUiState.currentPlayerIndex]
                            .chipBuyInAmount >= minimumRaise,
                onButtonClick = {
                    if(clientActionTurn && gameViewModel.timerProgress > 0.05) {
                        gameViewModel.isRaiseSlider = true
                    }
                }
            )
        }
        else {
            Column(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "$" + sliderPosition.toInt(),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .width(78.dp)
                        .background(color = Color(0xffad0516), shape = RoundedCornerShape(2.dp))
                        .padding(6.dp)
                )

                Slider(
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = if (it >= valueRange.endInclusive) {
                            valueRange.endInclusive
                        } else {
                            ((it / valueRange.start).roundToInt() * valueRange.start)
                        }
                    },
                    steps = steps,
                    valueRange = valueRange,
                    modifier = Modifier
                        .graphicsLayer {
                            rotationZ = 270f
                            transformOrigin = TransformOrigin(0f, 0f)
                        }
                        .layout { measurable, constraints ->
                            val placeable = measurable.measure(
                                Constraints(
                                    minWidth = constraints.minHeight,
                                    maxWidth = constraints.maxHeight,
                                    minHeight = constraints.minWidth,
                                    maxHeight = constraints.maxHeight,
                                )
                            )
                            layout(placeable.height, placeable.width) {
                                placeable.place(-placeable.width, 0)
                            }
                        }
                        .then(
                            Modifier
                                .fillMaxWidth(0.6f)
                                .background(
                                    color = Color(0x99000000),
                                    shape = RoundedCornerShape(5.dp)
                                )
                                .padding(15.dp)
                        ),
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xff961b1b),
                        activeTrackColor = Color(0xffe33636)
                    )
                )
                ActionButton(
                    buttonText = "CONFIRM",
                    buttonEnabled = gameUiState.isRaiseEnabled,
                    onButtonClick = {
                        if (clientActionTurn && gameViewModel.timerProgress > 0.05) {
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
}
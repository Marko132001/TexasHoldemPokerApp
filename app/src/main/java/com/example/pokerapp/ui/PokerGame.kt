package com.example.pokerapp.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokerapp.data.GameRound
import com.example.pokerapp.model.GameState

@Composable
fun PokerGame(
    context: Context,
    gameViewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    gameUiState: GameState,
    isConnecting: Boolean,
    showConnectionError: Boolean
){

    if(showConnectionError) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Couldn't connect to the server",
                color = Color.Red
            )
        }

    }

    if(isConnecting) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    if(gameUiState.players.size > 1) {

        LaunchedEffect(key1 = gameUiState.currentPlayerIndex, key2 = gameUiState.round) {
            gameViewModel.isRaiseSlider = false
        }

        TableBackground()

        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (communityCards, playerHand, opponentHand1,
                opponentHand2, opponentHand3, opponentHand4,
                actionButtons
            ) = createRefs()

            val (
                playerChipValue, opponentChipValue1, opponentChipValue2,
                opponentChipValue3, opponentChipValue4, potChipValue
            ) = createRefs()

            val (
                playerInfo, opponentInfo1, opponentInfo2,
                opponentInfo3, opponentInfo4
            ) = createRefs()

            CommunityCards(
                context,
                gameUiState,
                Modifier.constrainAs(communityCards) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
            )

            PotValue(
                modifier = Modifier
                    .constrainAs(potChipValue) {
                        centerHorizontallyTo(parent)
                    }
                    .padding(top = 90.dp),
                gameUiState.potAmount
            )

            CardHandPlayer(
                context,
                modifier = Modifier.constrainAs(playerHand) {
                    start.linkTo(actionButtons.start, margin = 10.dp)
                    bottom.linkTo(parent.bottom, margin = 30.dp)
                },
                chipValueModifier = Modifier.constrainAs(playerChipValue) {
                    end.linkTo(playerHand.start, margin = 10.dp)
                    bottom.linkTo(playerHand.top)
                },
                infoModifier = Modifier.constrainAs(playerInfo) {
                    end.linkTo(actionButtons.start)
                    bottom.linkTo(parent.bottom)
                },
                player = gameUiState.players[0],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 0)
                    gameUiState.dealerButtonPos
                else
                    -1,
                isActivePlayer =
                gameUiState.currentPlayerIndex == 0 && gameUiState.round != GameRound.SHOWDOWN,
                gameViewModel = gameViewModel
            )
            if (gameUiState.players.getOrNull(1) != null) {
                CardHandOpponent(
                    modifier = Modifier.constrainAs(opponentHand1) {
                        start.linkTo(parent.start, margin = 90.dp)
                        bottom.linkTo(parent.bottom, margin = 140.dp)
                    },
                    chipValueModifier = Modifier.constrainAs(opponentChipValue1) {
                        start.linkTo(opponentHand1.end, margin = 20.dp)
                        bottom.linkTo(opponentHand1.top)
                    },
                    infoModifier = Modifier.constrainAs(opponentInfo1) {
                        start.linkTo(parent.start, margin = 5.dp)
                        bottom.linkTo(parent.bottom, margin = 50.dp)
                    },
                    player = gameUiState.players[1],
                    dealerButtonPos =
                    if (gameUiState.dealerButtonPos == 1)
                        gameUiState.dealerButtonPos
                    else
                        -1,
                    isActivePlayer =
                    gameUiState.currentPlayerIndex == 1 && gameUiState.round != GameRound.SHOWDOWN,
                    context = context,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }
            if (gameUiState.players.getOrNull(2) != null) {
                CardHandOpponent(
                    modifier = Modifier.constrainAs(opponentHand2) {
                        top.linkTo(parent.top, margin = 70.dp)
                        start.linkTo(parent.start, margin = 125.dp)
                    },
                    chipValueModifier = Modifier.constrainAs(opponentChipValue2) {
                        start.linkTo(opponentHand2.end, margin = 30.dp)
                        bottom.linkTo(opponentHand2.bottom)
                    },
                    infoModifier = Modifier.constrainAs(opponentInfo2) {
                        start.linkTo(parent.start, margin = 5.dp)
                        top.linkTo(parent.top, margin = 5.dp)
                    },
                    player = gameUiState.players[2],
                    dealerButtonPos =
                    if (gameUiState.dealerButtonPos == 2)
                        gameUiState.dealerButtonPos
                    else
                        -1,
                    isActivePlayer =
                    gameUiState.currentPlayerIndex == 2 && gameUiState.round != GameRound.SHOWDOWN,
                    context = context,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }
            if (gameUiState.players.getOrNull(3) != null) {
                CardHandOpponent(
                    modifier = Modifier.constrainAs(opponentHand3) {
                        top.linkTo(parent.top, margin = 78.dp)
                        end.linkTo(parent.end, margin = 140.dp)
                    },
                    chipValueModifier = Modifier.constrainAs(opponentChipValue3) {
                        end.linkTo(opponentHand3.start, margin = 20.dp)
                        bottom.linkTo(opponentHand3.bottom)
                    },
                    infoModifier = Modifier.constrainAs(opponentInfo3) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                    },
                    player = gameUiState.players[3],
                    dealerButtonPos =
                    if (gameUiState.dealerButtonPos == 3)
                        gameUiState.dealerButtonPos
                    else
                        -1,
                    isActivePlayer =
                    gameUiState.currentPlayerIndex == 3 && gameUiState.round != GameRound.SHOWDOWN,
                    context = context,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }
            if (gameUiState.players.getOrNull(4) != null) {
                CardHandOpponent(
                    modifier = Modifier.constrainAs(opponentHand4) {
                        end.linkTo(parent.end, margin = 130.dp)
                        bottom.linkTo(parent.bottom, margin = 130.dp)
                    },
                    chipValueModifier = Modifier.constrainAs(opponentChipValue4) {
                        end.linkTo(opponentHand4.end)
                        bottom.linkTo(opponentHand4.top, margin = 15.dp)
                    },
                    infoModifier = Modifier.constrainAs(opponentInfo4) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 40.dp)
                    },
                    player = gameUiState.players[4],
                    dealerButtonPos =
                    if (gameUiState.dealerButtonPos == 4)
                        gameUiState.dealerButtonPos
                    else
                        -1,
                    context = context,
                    isActivePlayer =
                    gameUiState.currentPlayerIndex == 4 && gameUiState.round != GameRound.SHOWDOWN,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }

            ActionButtons(
                actionButtons = Modifier.constrainAs(actionButtons) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 3.dp)
                },
                gameViewModel,
                gameUiState
            )
        }

        if (gameViewModel.isRaiseSlider) {
            RaiseAmountSlider(
                minimumRaise = gameUiState.bigBlind.toFloat(),
                maximumRaise = (
                        gameUiState.players[gameUiState.currentPlayerIndex].chipBuyInAmount -
                                (gameUiState.currentHighBet - gameUiState.players[gameUiState.currentPlayerIndex].playerBet)
                        ).toFloat(),
                gameViewModel = gameViewModel
            )
        }
    }
    else{
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Waiting for more players...",
                color = Color.Red
            )
        }
    }

}
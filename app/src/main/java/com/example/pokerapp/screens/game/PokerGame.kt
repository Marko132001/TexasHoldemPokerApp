package com.example.pokerapp.screens.game

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.pokerapp.data.GameRound
import com.example.pokerapp.data.PlayerState
import com.example.pokerapp.model.GameState
import com.example.pokerapp.ui.components.game.ActionButtons
import com.example.pokerapp.ui.components.game.BuyInPopUpDialog
import com.example.pokerapp.ui.components.game.CardHandOpponent
import com.example.pokerapp.ui.components.game.CardHandPlayer
import com.example.pokerapp.ui.components.game.CommunityCards
import com.example.pokerapp.ui.components.game.InfoPopUpDialog
import com.example.pokerapp.ui.components.game.PotValue
import com.example.pokerapp.ui.components.game.TableBackground

@Composable
fun PokerGame(
    context: Context,
    openAndPopUp: (String, String) -> Unit,
    gameViewModel: GameViewModel,
    gameUiState: GameState,
    isConnecting: Boolean,
    showConnectionError: Boolean
){

    TableBackground()

    if(showConnectionError){
        InfoPopUpDialog (
            titleText = "Connection Failed",
            descriptionText = "Couldn't connect to the server, try again later.",
            buttonAction = {
                gameViewModel.onQuitGameClick(openAndPopUp)
            },
            buttonText = "QUIT",
            isDismissable = false,
            onDismiss = {}
        )
    }
    else if(isConnecting) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }


    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler(
        enabled = true,
        onBack = {
            if(!showExitDialog)
                showExitDialog = true
        }
    )

    if(showExitDialog){
        InfoPopUpDialog (
            titleText = "Quit Game",
            descriptionText = "Are you sure you want to exit the lobby?",
            buttonAction = {
                showExitDialog = false
                gameViewModel.onQuitGameClick(openAndPopUp)
            },
            buttonText = "QUIT",
            isDismissable = true,
            onDismiss = {
                showExitDialog = false
            }
        )
    }

    if(gameUiState.players.isNotEmpty()) {

        LaunchedEffect(key1 = gameUiState.playerSeatPositions) {
            gameViewModel.setCircularPlayerSeatOrder()
        }

        LaunchedEffect(key1 = gameUiState.currentPlayerIndex, key2 = gameUiState.round) {
            gameViewModel.isRaiseSlider = false
        }

        var showRebuyDialog by remember { mutableStateOf(false) }

        if(showRebuyDialog){
            BuyInPopUpDialog(
                minBuyIn = gameViewModel.minBuyIn,
                maxBuyIn = gameViewModel.maxBuyIn,
                userChips = gameViewModel.clientUser.value.chipAmount,
                openAndPopUp = openAndPopUp,
                onPlayClick = gameViewModel::onPlayClick,
                userDismissEnabled = false,
                onQuitGameClick = gameViewModel::onQuitGameClick,
                onDismissClick = { showRebuyDialog = false }
            )
        }


        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val (communityCards, playerHand, opponentHand1,
                opponentHand2, opponentHand3, opponentHand4,
                actionButtons, exitButton
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

            IconButton(
                modifier = Modifier
                    .constrainAs(exitButton) {
                        end.linkTo(parent.end, margin = 10.dp)
                        top.linkTo(parent.top, margin = 10.dp)
                    }
                    .background(color = Color(0xff4796d6), shape = RoundedCornerShape(10.dp))
                    .border(BorderStroke(1.5.dp, Color.White), shape = RoundedCornerShape(10.dp)),
                onClick = { showExitDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Filled.ExitToApp,
                    contentDescription = null,
                    tint = Color(0xfffcf99d),
                    modifier = Modifier
                        .size(28.dp)

                )
            }

            gameUiState.players.find { it.userId == gameViewModel.playerSeatPositions[0] }?.let {
                if(it.chipBuyInAmount == 0 && it.playerState == PlayerState.SPECTATOR
                    && !gameViewModel.isOnPlayClicked)
                    showRebuyDialog = true
                else if(it.chipBuyInAmount > 0 && gameViewModel.isOnPlayClicked) {
                    gameViewModel.isOnPlayClicked = false
                }

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
                    player = it,
                    dealerButtonPos =
                        if (gameUiState.players[gameUiState.dealerButtonPos].userId == it.userId)
                            gameUiState.dealerButtonPos
                        else
                            -1,
                    isActivePlayer =
                        gameUiState.players[gameUiState.currentPlayerIndex].userId == it.userId
                                && gameUiState.round != GameRound.SHOWDOWN
                                && gameUiState.isEnoughPlayers,
                    isEnoughPlayers = gameUiState.isEnoughPlayers,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }
            gameUiState.players.find { it.userId == gameViewModel.playerSeatPositions[1] }?.let {
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
                    player = it,
                    dealerButtonPos =
                        if (gameUiState.players[gameUiState.dealerButtonPos].userId == it.userId)
                            gameUiState.dealerButtonPos
                        else
                            -1,
                    isActivePlayer =
                        gameUiState.players[gameUiState.currentPlayerIndex].userId == it.userId
                                && gameUiState.round != GameRound.SHOWDOWN
                                && gameUiState.isEnoughPlayers,
                    context = context,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }
            gameUiState.players.find { it.userId == gameViewModel.playerSeatPositions[2] }?.let {
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
                    player = it,
                    dealerButtonPos =
                        if (gameUiState.players[gameUiState.dealerButtonPos].userId == it.userId)
                            gameUiState.dealerButtonPos
                        else
                            -1,
                    isActivePlayer =
                        gameUiState.players[gameUiState.currentPlayerIndex].userId == it.userId
                                && gameUiState.round != GameRound.SHOWDOWN
                                && gameUiState.isEnoughPlayers,
                    context = context,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }
            gameUiState.players.find { it.userId == gameViewModel.playerSeatPositions[3] }?.let {
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
                        end.linkTo(exitButton.start, margin = 20.dp)
                        top.linkTo(parent.top)
                    },
                    player = it,
                    dealerButtonPos =
                        if (gameUiState.players[gameUiState.dealerButtonPos].userId == it.userId)
                            gameUiState.dealerButtonPos
                        else
                            -1,
                    isActivePlayer =
                        gameUiState.players[gameUiState.currentPlayerIndex].userId == it.userId
                                && gameUiState.round != GameRound.SHOWDOWN
                                && gameUiState.isEnoughPlayers,
                    context = context,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }
            gameUiState.players.find { it.userId == gameViewModel.playerSeatPositions[4] }?.let {
                CardHandOpponent(
                    modifier = Modifier.constrainAs(opponentHand4) {
                        end.linkTo(parent.end, margin = 130.dp)
                        bottom.linkTo(parent.bottom, margin = 150.dp)
                    },
                    chipValueModifier = Modifier.constrainAs(opponentChipValue4) {
                        end.linkTo(opponentHand4.end)
                        bottom.linkTo(opponentHand4.top, margin = 15.dp)
                    },
                    infoModifier = Modifier.constrainAs(opponentInfo4) {
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 60.dp)
                    },
                    player = it,
                    dealerButtonPos =
                        if (gameUiState.players[gameUiState.dealerButtonPos].userId == it.userId)
                            gameUiState.dealerButtonPos
                        else
                            -1,
                    context = context,
                    isActivePlayer =
                        gameUiState.players[gameUiState.currentPlayerIndex].userId == it.userId
                                && gameUiState.round != GameRound.SHOWDOWN
                                && gameUiState.isEnoughPlayers,
                    round = gameUiState.round,
                    gameViewModel = gameViewModel
                )
            }

            ActionButtons(
                actionButtonsModifier = Modifier.constrainAs(actionButtons) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end, margin = 15.dp)
                },
                gameViewModel = gameViewModel,
                gameUiState = gameUiState,
                clientActionTurn = gameUiState.players[gameUiState.currentPlayerIndex].userId
                        == gameViewModel.clientUserId && gameUiState.players.size > 1,
                minimumRaise = gameUiState.bigBlind.toFloat(),
                maximumRaise = (
                        gameUiState.players[gameUiState.currentPlayerIndex].chipBuyInAmount -
                                (gameUiState.currentHighBet - gameUiState.players[gameUiState.currentPlayerIndex].playerBet)
                        ).toFloat(),
            )
        }

        if(!gameUiState.isEnoughPlayers && !showRebuyDialog){
            InfoPopUpDialog (
                titleText = "Waiting for players",
                descriptionText = "Please wait for more players to join...",
                buttonAction = {
                    gameViewModel.onQuitGameClick(openAndPopUp)
                },
                buttonText = "QUIT",
                isDismissable = false,
                onDismiss = {}
            )
        }
    }

}
package com.example.projectapp

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.data.GameRound
import com.example.projectapp.data.HandRankings
import com.example.projectapp.data.PlayerState
import com.example.projectapp.model.Player
import com.example.projectapp.ui.GameUiState
import com.example.projectapp.ui.GameViewModel
import com.example.projectapp.ui.SliderWithLabel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                PokerApp()
            }
        }
    }
}


@Composable
fun PokerApp(
    gameViewModel: GameViewModel = viewModel()
){

    val context = LocalContext.current
    val activity = context as Activity
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    val gameUiState by gameViewModel.uiState.collectAsStateWithLifecycle()


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
            Modifier.constrainAs(communityCards){
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
            modifier = Modifier.constrainAs(playerHand){
                start.linkTo(actionButtons.start, margin = 10.dp)
                bottom.linkTo(parent.bottom, margin = 30.dp)
            },
            chipValueModifier = Modifier.constrainAs(playerChipValue){
                end.linkTo(playerHand.start, margin = 10.dp)
                bottom.linkTo(playerHand.top)
            },
            infoModifier = Modifier.constrainAs(playerInfo){
                end.linkTo(actionButtons.start)
                bottom.linkTo(parent.bottom)
            },
            player = gameUiState.players[0],
            dealerButtonPos =
            if (gameUiState.dealerButtonPos == 0)
                gameUiState.dealerButtonPos
            else
                -1
        )
        if(gameUiState.players.getOrNull(1) != null) {
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
                    end.linkTo(opponentHand1.end, margin = (-60).dp)
                    top.linkTo(opponentHand1.bottom)
                },
                player = gameUiState.players[1],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 1)
                    gameUiState.dealerButtonPos
                else
                    -1,
                context = context,
                round = gameUiState.round
            )
        }
        if(gameUiState.players.getOrNull(2) != null) {
            CardHandOpponent(
                modifier = Modifier.constrainAs(opponentHand2) {
                    top.linkTo(parent.top, margin = 70.dp)
                    start.linkTo(opponentHand1.start, margin = 30.dp)
                },
                chipValueModifier = Modifier.constrainAs(opponentChipValue2) {
                    start.linkTo(opponentHand2.end, margin = 30.dp)
                    bottom.linkTo(opponentHand2.bottom)
                },
                infoModifier = Modifier.constrainAs(opponentInfo2) {
                    end.linkTo(opponentHand2.end, margin = (-30).dp)
                    bottom.linkTo(opponentHand2.top, margin = (-30).dp)
                },
                player = gameUiState.players[2],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 2)
                    gameUiState.dealerButtonPos
                else
                    -1,
                context = context,
                round = gameUiState.round
            )
        }
        if(gameUiState.players.getOrNull(3) != null) {
            CardHandOpponent(
                modifier = Modifier.constrainAs(opponentHand3) {
                    top.linkTo(parent.top, margin = 70.dp)
                    end.linkTo(opponentHand4.start, margin = 10.dp)
                },
                chipValueModifier = Modifier.constrainAs(opponentChipValue3) {
                    end.linkTo(opponentHand3.start, margin = 20.dp)
                    bottom.linkTo(opponentHand3.bottom)
                },
                infoModifier = Modifier.constrainAs(opponentInfo3) {
                    start.linkTo(opponentHand3.end)
                    bottom.linkTo(opponentHand3.top, margin = (-30).dp)
                },
                player = gameUiState.players[3],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 3)
                    gameUiState.dealerButtonPos
                else
                    -1,
                context = context,
                round = gameUiState.round
            )
        }
        if(gameUiState.players.getOrNull(4) != null) {
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
                    start.linkTo(opponentHand4.start, margin = (-30).dp)
                    top.linkTo(opponentHand4.bottom)
                },
                player = gameUiState.players[4],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 4)
                    gameUiState.dealerButtonPos
                else
                    -1,
                context = context,
                round = gameUiState.round
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

    if(gameViewModel.isRaiseSlider) {
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

@Composable
fun ActionButtons(
    actionButtons: Modifier,
    gameViewModel: GameViewModel,
    gameUiState: GameUiState
) {

    Row(
        modifier = actionButtons,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Button(
            onClick = {
                gameViewModel.handleFoldAction()
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
                gameViewModel.handleCheckAction()
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
                gameViewModel.handleCallAction()
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
                    gameViewModel.isRaiseSlider = true
                },
                enabled = gameUiState.isRaiseEnabled,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(text = "RAISE")
            }
        }
        else{
            Button(
                onClick = {
                    gameViewModel.handleRaiseAction()
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

@Composable
fun PotValue(modifier: Modifier, potAmount: Int) {

    Row (
        modifier = modifier.background(color = Color.Black, shape = RoundedCornerShape(45)),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(R.drawable.pot_chip),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = potAmount.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp,
            color = Color.White
        )
    }
}

@Composable
fun ChipValue(modifier: Modifier, chipsAmount: Int) {

    Row (
        modifier = modifier.background(color = Color(0xFFFFFFBF), shape = CircleShape),
        verticalAlignment = Alignment.CenterVertically
    ){
        Image(
            modifier = Modifier.size(22.dp),
            painter = painterResource(R.drawable.poker_chip),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = chipsAmount.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
    }
}

@Composable
fun TableBackground(){
    Image(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xff781008)),
        painter = painterResource(R.drawable.poker_table),
        contentScale = ContentScale.FillBounds,
        contentDescription = null
    )
}

@Composable
fun PlayerInformation(
    username: String,
    playerChips: Int,
    playerState: PlayerState,
    playerHandRank: HandRankings,
    dealerButtonPos: Int
) {
    Column (Modifier.padding(10.dp)){
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color(0x80030303)
            ),
            border =
                if(playerState.name == PlayerState.WINNER.name)
                    BorderStroke(2.dp, Color(0xffbdb824))
                else
                    BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(10),
            modifier = Modifier
                .width(150.dp)
                .height(45.dp)
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.unknown),
                    contentDescription = null,
                    alignment = Alignment.Center
                )

                Column {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        text = username,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 13.sp
                    )

                    Text(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        text = playerChips.toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

            }
        }

        Text(
            text =
            if(playerState.name == PlayerState.WINNER.name)
                playerHandRank.label
            else if (playerState.name != PlayerState.INACTIVE.name
                && playerState.name != PlayerState.SPECTATOR.name)
                playerState.label
            else
                "",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xffbdb824)
        )
    }

    if(dealerButtonPos != -1) {
        Image(
            modifier = Modifier.size(20.dp),
            painter = painterResource(R.drawable.dealer_button),
            contentDescription = null,
        )
    }

}



@Composable
fun CardHandPlayer(
    context: Context,
    modifier: Modifier,
    chipValueModifier: Modifier,
    infoModifier: Modifier,
    player: Player,
    dealerButtonPos: Int
){

    val holeCards = player.getHoleCardsLabels()

    val firstCardId: Int = remember(holeCards.first) {
        context.resources.getIdentifier(
            holeCards.first,
            "drawable",
            context.packageName
        )
    }

    val secondCardId: Int = remember(holeCards.second) {
        context.resources.getIdentifier(
            holeCards.second,
            "drawable",
            context.packageName
        )
    }

    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .size(80.dp),
            painter = painterResource(id = firstCardId),
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(80.dp)
                .absoluteOffset(x = 45.dp, y = 5.dp)
                .rotate(10.0F),
            painter = painterResource(id = secondCardId),
            contentDescription = null
        )
    }

    Box (
        modifier = infoModifier
    ){
        PlayerInformation(
            player.user.username,
            player.chipBuyInAmount,
            player.playerState,
            player.playerHandRank.first,
            dealerButtonPos
        )
    }

    if(player.playerBet > 0){
        ChipValue(
            modifier = chipValueModifier,
            chipsAmount = player.playerBet
        )
    }
}

@Composable
fun CardHandOpponent(
    modifier: Modifier,
    chipValueModifier: Modifier,
    infoModifier: Modifier,
    player: Player,
    dealerButtonPos: Int,
    context: Context,
    round: GameRound
){

    if(round == GameRound.SHOWDOWN &&
        (player.playerState != PlayerState.FOLD && player.playerState != PlayerState.SPECTATOR)
    ){
        val holeCards = player.getHoleCardsLabels()

        val firstCardId: Int = remember(holeCards.first) {
            context.resources.getIdentifier(
                holeCards.first,
                "drawable",
                context.packageName
            )
        }

        val secondCardId: Int = remember(holeCards.second) {
            context.resources.getIdentifier(
                holeCards.second,
                "drawable",
                context.packageName
            )
        }

        Box(
            modifier = modifier
        ) {
            Image(
                modifier = Modifier
                    .size(60.dp),
                painter = painterResource(id = firstCardId),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(60.dp)
                    .absoluteOffset(x = 35.dp, y = 5.dp)
                    .rotate(10.0F),
                painter = painterResource(id = secondCardId),
                contentDescription = null
            )
        }
    }
    else{
        val opponentCard = painterResource(R.drawable.blue2)

        Box(
            modifier = modifier
        ) {
            Image(
                modifier = Modifier
                    .size(30.dp),
                painter = opponentCard,
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .size(30.dp)
                    .absoluteOffset(x = 20.dp)
                    .rotate(4.0F),
                painter = opponentCard,
                contentDescription = null
            )
        }
    }


    Box (
        modifier = infoModifier
    ){
        PlayerInformation(
            player.user.username,
            player.chipBuyInAmount,
            player.playerState,
            player.playerHandRank.first,
            dealerButtonPos
        )
    }

    if(player.playerBet > 0){
        ChipValue(
            modifier = chipValueModifier,
            chipsAmount = player.playerBet
        )
    }
}

@Composable
fun CommunityCards(context: Context, gameUiState: GameUiState, modifier: Modifier) {

    val communityCardIds: MutableList<Int> = mutableListOf()

    gameUiState.communityCards.forEach { communityCard ->
        val cardId: Int = remember(communityCard) {
            context.resources.getIdentifier(
                communityCard,
                "drawable",
                context.packageName
            )
        }

        communityCardIds.add(cardId)
    }

    LazyRow(
        modifier = modifier
    ) {
        if(communityCardIds.isNotEmpty()) {
            items(communityCardIds) { cardId ->
                Image(
                    modifier = Modifier
                        .size(65.dp),
                    painter = painterResource(id = cardId),
                    contentDescription = null
                )
            }
        }
    }

}






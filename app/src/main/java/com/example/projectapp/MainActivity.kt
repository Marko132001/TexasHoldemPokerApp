package com.example.projectapp

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderPositions
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.data.PlayerState
import com.example.projectapp.ui.GameUiState
import com.example.projectapp.ui.GameViewModel
import com.example.projectapp.ui.SliderWithLabel
import kotlin.math.roundToInt


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
            holeCards = gameUiState.holeCards,
            chipsAmount = gameUiState.playerBets[0],
            username = gameUiState.playerUserNames[0],
            playerChips = gameUiState.playersBuyInChips[0],
            playerState = gameUiState.playerStates[0],
            dealerButtonPos =
            if (gameUiState.dealerButtonPos == 0)
                gameUiState.dealerButtonPos
            else
                -1
        )
        if(gameUiState.playerUserNames.getOrNull(1) != null) {
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
                chipsAmount = gameUiState.playerBets[1],
                username = gameUiState.playerUserNames[1],
                playerChips = gameUiState.playersBuyInChips[1],
                playerState = gameUiState.playerStates[1],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 1)
                    gameUiState.dealerButtonPos
                else
                    -1
            )
        }
        if(gameUiState.playerUserNames.getOrNull(2) != null) {
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
                chipsAmount = gameUiState.playerBets[2],
                username = gameUiState.playerUserNames[2],
                playerChips = gameUiState.playersBuyInChips[2],
                playerState = gameUiState.playerStates[2],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 2)
                    gameUiState.dealerButtonPos
                else
                    -1
            )
        }
        if(gameUiState.playerUserNames.getOrNull(3) != null) {
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
                chipsAmount = gameUiState.playerBets[3],
                username = gameUiState.playerUserNames[3],
                playerChips = gameUiState.playersBuyInChips[3],
                playerState = gameUiState.playerStates[3],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 3)
                    gameUiState.dealerButtonPos
                else
                    -1
            )
        }
        if(gameUiState.playerUserNames.getOrNull(4) != null) {
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
                chipsAmount = gameUiState.playerBets[4],
                username = gameUiState.playerUserNames[4],
                playerChips = gameUiState.playersBuyInChips[4],
                playerState = gameUiState.playerStates[4],
                dealerButtonPos =
                if (gameUiState.dealerButtonPos == 4)
                    gameUiState.dealerButtonPos
                else
                    -1
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
            maximumRaise = gameUiState.currentPlayerChips.toFloat(),
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
    dealerButtonPos: Int
) {
    Column (Modifier.padding(10.dp)){
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color(0x80030303)
            ),
            border = BorderStroke(1.dp, Color.Black),
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
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text =
            if (playerState.name != PlayerState.NONE.name)
                playerState.name
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
    holeCards: Pair<String, String>,
    chipsAmount: Int,
    username: String,
    playerChips: Int,
    playerState: PlayerState,
    dealerButtonPos: Int
){

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
            username,
            playerChips,
            playerState,
            dealerButtonPos
        )
    }

    if(chipsAmount > 0){
        ChipValue(
            modifier = chipValueModifier,
            chipsAmount
        )
    }
}

@Composable
fun CardHandOpponent(
    modifier: Modifier,
    chipValueModifier: Modifier,
    infoModifier: Modifier,
    chipsAmount: Int,
    username: String,
    playerChips: Int,
    playerState: PlayerState,
    dealerButtonPos: Int
){
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

    Box (
        modifier = infoModifier
    ){
        PlayerInformation(
            username,
            playerChips,
            playerState,
            dealerButtonPos
        )
    }

    if(chipsAmount > 0){
        ChipValue(
            modifier = chipValueModifier,
            chipsAmount = chipsAmount
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






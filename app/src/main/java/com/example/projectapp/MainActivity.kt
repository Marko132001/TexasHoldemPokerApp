package com.example.projectapp

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.data.PlayerState
import com.example.projectapp.ui.GameUiState
import com.example.projectapp.ui.GameViewModel
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
            actionButtons,
            playerChipValue, opponentChipValue1, opponentChipValue2,
            opponentChipValue3, opponentChipValue4, potChipValue
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
                .padding(top = 55.dp),
            gameUiState.potAmount
        )

        CardHandPlayer(
            context,
            gameUiState,
            modifier = Modifier.constrainAs(playerHand){
                end.linkTo(actionButtons.start, margin = 60.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
            },
            chipValueModifier = Modifier.constrainAs(playerChipValue){
                start.linkTo(playerHand.end, margin = 60.dp)
                bottom.linkTo(playerHand.top)
            }
        )

        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand1){
                centerVerticallyTo(communityCards)
                start.linkTo(parent.start, margin = 20.dp)
            },
            chipValueModifier = Modifier.constrainAs(opponentChipValue1){
                start.linkTo(opponentHand1.end, margin = 25.dp)
                top.linkTo(opponentHand1.bottom)
            },
            gameUiState.playerBets[1]
        )
        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand2){
                top.linkTo(parent.top, margin = 20.dp)
                start.linkTo(opponentHand1.end, margin = 140.dp)
            },
            chipValueModifier = Modifier.constrainAs(opponentChipValue2){
                start.linkTo(opponentHand2.start)
                top.linkTo(opponentHand2.bottom, margin = 20.dp)
            },
            gameUiState.playerBets[2]
        )
        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand3){
                top.linkTo(parent.top, margin = 20.dp)
                end.linkTo(opponentHand4.start, margin = 140.dp)
            },
            chipValueModifier = Modifier.constrainAs(opponentChipValue3){
                start.linkTo(opponentHand3.end, margin = 25.dp)
                top.linkTo(opponentHand3.bottom, margin = 20.dp)
            },
            chipsAmount = 0
        )
        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand4){
                centerVerticallyTo(communityCards)
                end.linkTo(parent.end, margin = 40.dp)
            },
            chipValueModifier = Modifier.constrainAs(opponentChipValue4){
                end.linkTo(opponentHand4.end)
                top.linkTo(opponentHand1.bottom, margin = 10.dp)
            },
            chipsAmount = 0
        )

        ActionButtons(
            Modifier.constrainAs(actionButtons) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 3.dp)
            },
            gameViewModel,
            gameUiState
        )
    }

}

@Composable
fun ActionButtons(
    actionButtons: Modifier,
    gameViewModel: GameViewModel,
    gameUiState: GameUiState
) {

    var activeRaiseSlider by remember { mutableStateOf(false) }

    Row(
        modifier = actionButtons,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Button(
            onClick = {
                gameViewModel.handleFoldAction()
                activeRaiseSlider = false
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
                activeRaiseSlider = false
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
                activeRaiseSlider = false
            },
            enabled = gameUiState.isCallEnabled,
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray
            )
        ) {
            Text(text = "CALL")
        }
        if(!activeRaiseSlider) {
            Button(
                onClick = {
                    activeRaiseSlider = true
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
            Column {
                RaiseAmountSlider(
                    minimumRaise = gameUiState.bigBlind.toFloat(),
                    maximumRaise = gameUiState.currentPlayerChips.toFloat(),
                    gameViewModel = gameViewModel
                )
                Button(
                    onClick = {
                        activeRaiseSlider = false
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
}

@Composable
fun RaiseAmountSlider(
    minimumRaise: Float,
    maximumRaise: Float,
    gameViewModel: GameViewModel
) {
    //TODO: Finish slider UI
    var sliderPosition by remember { mutableFloatStateOf(minimumRaise) }
    gameViewModel.raiseAmount = sliderPosition.toInt()

    Box (
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column {
            Slider(
                modifier = Modifier.rotate(270.0f).size(300.dp),
                value = sliderPosition,
                onValueChange = { sliderPosition = it.roundToInt().toFloat() },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.secondary,
                    activeTrackColor = MaterialTheme.colorScheme.secondary,
                    inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                steps = 3,
                valueRange = minimumRaise..maximumRaise
            )
            Text(text = sliderPosition.toString())
        }
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
            modifier = Modifier.size(25.dp),
            painter = painterResource(R.drawable.poker_chip),
            contentDescription = null
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 10.dp),
            text = chipsAmount.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
fun TableBackground(){
    Image(
        modifier = Modifier
        .fillMaxSize(),
        painter = painterResource(R.drawable.table),
        contentScale = ContentScale.FillBounds,
        contentDescription = null
    )
}



@Composable
fun CardHandPlayer(
    context: Context,
    gameUiState: GameUiState,
    modifier: Modifier,
    chipValueModifier: Modifier
){

    val firstCardId: Int = remember(gameUiState.holeCards.first) {
        context.resources.getIdentifier(
            gameUiState.holeCards.first,
            "drawable",
            context.packageName
        )
    }

    val secondCardId: Int = remember(gameUiState.holeCards.second) {
        context.resources.getIdentifier(
            gameUiState.holeCards.second,
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

    if(gameUiState.playerBets[0] > 0){
        ChipValue(
            modifier = chipValueModifier,
            gameUiState.playerBets[0]
        )
    }
}

@Composable
fun CardHandOpponent(modifier: Modifier, chipValueModifier: Modifier, chipsAmount: Int){
    val opponentCard = painterResource(R.drawable.blue2)
    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .size(40.dp),
            painter = opponentCard,
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(40.dp)
                .absoluteOffset(x = 20.dp)
                .rotate(4.0F),
            painter = opponentCard,
            contentDescription = null
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
                        .size(80.dp),
                    painter = painterResource(id = cardId),
                    contentDescription = null
                )
            }
        }
    }

}






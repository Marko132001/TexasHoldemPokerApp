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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.projectapp.data.GameRound
import com.example.projectapp.data.PlayingCard
import com.example.projectapp.model.Game
import com.example.projectapp.model.Player
import com.example.projectapp.model.User
import kotlin.concurrent.fixedRateTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.ui.GameViewModel


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

    TableBackground()
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (communityCards, playerHand, opponentHand1,
            opponentHand2, opponentHand3, opponentHand4,
            actionButtons
        ) = createRefs()

        LazyRow(
            modifier = Modifier
                .constrainAs(communityCards){
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                }
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

        CardHandPlayer(
            Pair(firstCardId, secondCardId),
            modifier = Modifier.constrainAs(playerHand){
                //centerHorizontallyTo(parent)
                end.linkTo(actionButtons.start, margin = 60.dp)
                bottom.linkTo(parent.bottom, margin = 10.dp)
            }
        )
        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand1){
                centerVerticallyTo(communityCards)
                end.linkTo(parent.end, margin = 40.dp)
            }
        )
        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand2){
                centerVerticallyTo(communityCards)
                start.linkTo(parent.start, margin = 20.dp)
            }
        )
        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand3){
                top.linkTo(parent.top, margin = 20.dp)
                end.linkTo(communityCards.start)
            }
        )
        CardHandOpponent(
            modifier = Modifier.constrainAs(opponentHand4){
                top.linkTo(parent.top, margin = 20.dp)
                start.linkTo(communityCards.end)
            }
        )

        Row(
            modifier = Modifier.constrainAs(actionButtons){
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, margin = 3.dp)
            },
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Button(
                onClick = { gameViewModel.handleFoldAction() },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(text = "FOLD")
            }
            Button(
                onClick = { gameViewModel.handleCheckAction() },
                enabled = gameUiState.isCheckEnabled,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(text = "CHECK")
            }
            Button(
                onClick = { gameViewModel.handleCallAction() },
                enabled = gameUiState.isCallEnabled,
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(text = "CALL")
            }
            Button(
                onClick = {
                    //TODO: Raise amount user input
                    gameViewModel.handleRaiseAction()
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

    }

}

@Composable
fun TableBackground(){
    val table = painterResource(R.drawable.table)

    Image(modifier = Modifier
        .fillMaxSize(),
        painter = table,
        contentScale = ContentScale.FillBounds,
        contentDescription = null)
}



@Composable
fun CardHandPlayer(playerCardIds: Pair<Int, Int>, modifier: Modifier){

    val firstCard = painterResource(id = playerCardIds.first)
    val secondCard = painterResource(id = playerCardIds.second)

    Box(
        modifier = modifier
    ) {
        Image(
            modifier = Modifier
                .size(80.dp),
            painter = firstCard,
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(80.dp)
                .absoluteOffset(x = 45.dp, y = 5.dp)
                .rotate(10.0F),
            painter = secondCard,
            contentDescription = null
        )
    }
}

@Composable
fun CardHandOpponent(modifier: Modifier){
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
}






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
import com.example.projectapp.data.PlayingCard
import com.example.projectapp.model.Game
import com.example.projectapp.model.Player
import com.example.projectapp.model.User


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

fun initGame() {

    val user1 = User("User1")
    val user2 = User("User2")
    val user3 = User("User3")

    val player1 = Player(user1, chipBuyInAmount = 500)
    val player2 = Player(user2, chipBuyInAmount = 900)
    val player3 = Player(user3, chipBuyInAmount = 1000)

    val game = Game()

    game.playerJoin(player1)
    game.playerJoin(player2)
    game.playerJoin(player3)

}

@Composable
fun PokerApp(){

    val context = LocalContext.current
    val activity = context as Activity
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    val cards: MutableList<PlayingCard> = mutableListOf(
        PlayingCard.TEN_OF_SPADES,
        PlayingCard.TWO_OF_DIAMONDS,
        PlayingCard.FIVE_OF_SPADES,
        PlayingCard.ACE_OF_HEARTS,
        PlayingCard.QUEEN_OF_SPADES
    )

    val cardIds: MutableList<Int> = mutableListOf()

    cards.forEach { playingCard ->
        val cardLabel = playingCard.suit.label + "_" + playingCard.rank.label
        val cardId: Int = remember(cardLabel) {
            context.resources.getIdentifier(
                cardLabel,
                "drawable",
                context.packageName
            )
        }

        cardIds.add(cardId)
    }

    TableBackground()
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (communityCards, playerHand, opponentHand1,
            opponentHand2, opponentHand3, opponentHand4,
            actionButtons
        ) = createRefs()

        LazyRow(modifier = Modifier
            .constrainAs(communityCards){
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }
        ) {
            items(cardIds) { cardId ->
                Image(
                    modifier = Modifier
                        .size(80.dp),
                    painter = painterResource(id = cardId),
                    contentDescription = null
                )
            }
        }

        CardHandPlayer(context,
            modifier = Modifier.constrainAs(playerHand){
                centerHorizontallyTo(parent)
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
                end.linkTo(parent.end, margin = 10.dp)
            },
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ){
            Button(
                onClick = { /*TODO*/ },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(text = "FOLD")
            }
            Button(
                onClick = { /*TODO*/ },
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(text = "CHECK")
            }
            Button(
                onClick = { /*TODO*/ },
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
fun CardHandPlayer(context: Context, modifier: Modifier){

    val firstCardLabel: String =
        PlayingCard.TEN_OF_SPADES.suit.label + "_" + PlayingCard.TEN_OF_SPADES.rank.label
    val secondCardLabel: String =
        PlayingCard.KING_OF_CLUBS.suit.label + "_" + PlayingCard.KING_OF_CLUBS.rank.label


    val firstCardId: Int = remember(firstCardLabel) {
        context.resources.getIdentifier(
            firstCardLabel,
            "drawable",
            context.packageName
        )
    }

    val secondCardId: Int = remember(secondCardLabel) {
        context.resources.getIdentifier(
            secondCardLabel,
            "drawable",
            context.packageName
        )
    }

    val firstCard = painterResource(id = firstCardId)
    val secondCard = painterResource(id = secondCardId)

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






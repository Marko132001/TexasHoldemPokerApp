package com.example.projectapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.example.projectapp.data.PlayingCard


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
fun PokerApp(){

    val context = LocalContext.current
    val activity = context as Activity
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    TableBackground()
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardHandOpponent()
        Spacer(modifier = Modifier.weight(1f))
        CardHandPlayer(context)
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
fun CardHandPlayer(context: Context){

    val firstCardLabel: String =
        PlayingCard.TEN_OF_DIAMONDS.suit.label + "_" + PlayingCard.TEN_OF_DIAMONDS.rank.label
    val secondCardLabel: String =
        PlayingCard.KING_OF_CLUBS.suit.label + "_" + PlayingCard.KING_OF_CLUBS.rank.label

    //val context = LocalContext.current
    /* Alternative with remember
    val drawableId = remember(name) {
        context.resources.getIdentifier(
            name,
            "drawable",
            context.packageName
        )
    }
    */
    val firstCardId: Int = context.resources.getIdentifier(
        firstCardLabel,
        "drawable",
        context.packageName
    )

    val secondCardId: Int = context.resources.getIdentifier(
        secondCardLabel,
        "drawable",
        context.packageName
    )

    val firstCard = painterResource(id = firstCardId)
    val secondCard = painterResource(id = secondCardId)

    Box(contentAlignment = Alignment.BottomCenter) {
        Image(
            modifier = Modifier
                .size(90.dp)
                .absolutePadding(bottom = 20.dp),
            painter = firstCard,
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(90.dp)
                .absoluteOffset(x = 45.dp, y = 5.dp)
                .rotate(10.0F)
                .absolutePadding(bottom = 20.dp),
            painter = secondCard,
            contentDescription = null
        )
    }
}

@Composable
fun CardHandOpponent(){
    val opponentCard = painterResource(R.drawable.blue2)
    Box(contentAlignment = Alignment.BottomCenter) {
        Image(
            modifier = Modifier
                .size(90.dp)
                .absolutePadding(bottom = 20.dp),
            painter = opponentCard,
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(90.dp)
                .absoluteOffset(x = 45.dp, y = 5.dp)
                .rotate(10.0F)
                .absolutePadding(bottom = 20.dp),
            painter = opponentCard,
            contentDescription = null
        )
    }
    /*
    Box(contentAlignment = Alignment.BottomCenter) {
        Image(
            modifier = Modifier
                .size(90.dp)
                .rotate(10.0F)
                .absolutePadding(top = 20.dp),
            painter = opponentCard,
            contentDescription = null
        )

        Image(
            modifier = Modifier
                .size(90.dp)
                .absoluteOffset(x = 45.dp, y = 5.dp)
                .absolutePadding(top = 20.dp),
            painter = opponentCard,
            contentDescription = null
        )
    }

     */
}

@Composable
fun CommunityCards(){
    Row {

    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PokerAppPreview() {
    TableBackground()
    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardHandOpponent()
        Spacer(modifier = Modifier.weight(1f))
        CardHandPlayer()
    }
}
*/






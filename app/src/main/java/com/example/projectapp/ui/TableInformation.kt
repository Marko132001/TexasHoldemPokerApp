package com.example.projectapp.ui

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectapp.R
import com.example.projectapp.model.GameState

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
fun CommunityCards(context: Context, gameUiState: GameState, modifier: Modifier) {

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

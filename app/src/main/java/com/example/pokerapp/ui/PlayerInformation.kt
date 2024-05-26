package com.example.pokerapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokerapp.R
import com.example.pokerapp.data.GameRound
import com.example.pokerapp.data.PlayerState
import com.example.pokerapp.model.PlayerDataState
import kotlinx.coroutines.launch

@Composable
fun CardHandPlayer(
    context: Context,
    modifier: Modifier,
    chipValueModifier: Modifier,
    infoModifier: Modifier,
    player: PlayerDataState,
    dealerButtonPos: Int,
    isActivePlayer: Boolean,
    gameViewModel: GameViewModel
){

    val holeCards = player.holeCards

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
            player.username,
            player.chipBuyInAmount,
            player.playerState,
            player.playerHandRank,
            dealerButtonPos,
            isActivePlayer,
            gameViewModel
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
    player: PlayerDataState,
    dealerButtonPos: Int,
    isActivePlayer: Boolean,
    context: Context,
    round: GameRound,
    gameViewModel: GameViewModel
){

    if(round == GameRound.SHOWDOWN &&
        (player.playerState != PlayerState.FOLD && player.playerState != PlayerState.SPECTATOR)
    ){
        val holeCards = player.holeCards

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
            player.username,
            player.chipBuyInAmount,
            player.playerState,
            player.playerHandRank,
            dealerButtonPos,
            isActivePlayer,
            gameViewModel
        )
    }

    if(player.playerBet > 0){
        ChipValue(
            modifier = chipValueModifier,
            chipsAmount = player.playerBet
        )
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PlayerInformation(
    username: String,
    playerChips: Int,
    playerState: PlayerState,
    playerHandRank: String,
    dealerButtonPos: Int,
    isActivePlayer: Boolean,
    gameViewModel: GameViewModel
) {

    if(isActivePlayer){
        val scope = rememberCoroutineScope()
        gameViewModel.timerProgress = 1.0f

        scope.launch {
            gameViewModel.timerCountdown()
        }
    }

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

                Box {
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

                    if (isActivePlayer) {
                        val animatedProgress = animateFloatAsState(
                            targetValue = gameViewModel.timerProgress,
                            animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
                        ).value

                        LinearProgressIndicator(
                            progress = animatedProgress,
                            modifier = Modifier
                                .fillMaxSize(),
                            color = Color.Yellow.copy(alpha = 0.2F),
                            trackColor = Color.Transparent
                        )

                    }
                }

            }
        }

        Text(
            text =
            if(playerState.name == PlayerState.WINNER.name)
                playerHandRank
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
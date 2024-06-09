package com.example.pokerapp.ui.components.game

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.pokerapp.R
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round

@Composable
fun BuyInPopUpDialog(
    minBuyIn: Int,
    maxBuyIn: Int,
    userChips: Int,
    openAndPopUp: (String, String) -> Unit,
    onPlayClick: ((String, String) -> Unit, Int) -> Unit,
    userDismissEnabled: Boolean,
    onQuitGameClick: ((String, String) -> Unit) -> Unit,
    onDismissClick: () -> Unit
){

    var sliderPosition by remember { mutableFloatStateOf(0f) }
    var selectBuyInValue by remember { mutableFloatStateOf(minBuyIn.toFloat()) }

    val sliderStep = 10f.pow(minBuyIn.toString().length - 2)
    var maxBuyIn = maxBuyIn.toFloat()

    if(userChips < maxBuyIn){
        maxBuyIn = floor(userChips.toFloat()/sliderStep) * sliderStep
    }

    var steps = 0
    var rangeSteps = 0
    if(maxBuyIn > minBuyIn){
        rangeSteps = ((maxBuyIn - minBuyIn.toFloat()) / sliderStep).toInt()
        steps = rangeSteps - 1
    }
    Log.d("POPUPDIALOG", "MaxBuyIn: $maxBuyIn, MinBuyIn: $minBuyIn, Slider step: $sliderStep, Steps: $steps")

    Dialog(
        onDismissRequest = { onDismissClick() },
        properties = DialogProperties(
            dismissOnClickOutside = userDismissEnabled,
            dismissOnBackPress = userDismissEnabled
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp)
                .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Row (
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(Color(0xff78b52d))
                        .shadow(1.dp),
                ){
                    Text(
                        text = "Pick your buy-in",
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    modifier = Modifier
                        .background(color = Color.DarkGray, shape = RoundedCornerShape(45))
                        .padding(horizontal = 25.dp),
                    text = "$" + selectBuyInValue.toInt().toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color.White
                )
                Slider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    value = sliderPosition,
                    onValueChange = {
                        sliderPosition = it
                        selectBuyInValue = minBuyIn + (round(it) * sliderStep)
                    },
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = Color.DarkGray,
                    ),
                    steps = steps,
                    valueRange = 0f..rangeSteps.toFloat()
                )
                Text(
                    text = "Blinds",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(R.drawable.poker_chip),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        modifier = Modifier.absolutePadding(right = 18.dp),
                        text = "$25 / $50",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }

            }
        }

        Box (
            modifier = Modifier
                .absoluteOffset(
                    x = if(!userDismissEnabled) 80.dp else 130.dp,
                    y = 170.dp
                )
        ){
            Row {
                Button(
                    onClick = {
                        onDismissClick()
                        onPlayClick(openAndPopUp, selectBuyInValue.toInt())
                    },
                    modifier = Modifier
                        .width(65.dp)
                        .background(color = Color(0xffde7621), shape = RoundedCornerShape(50.dp))
                        .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(50.dp)),
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "PLAY",
                        color = Color.White,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                if(!userDismissEnabled){
                    Spacer(modifier = Modifier.width(33.dp))
                    Button(
                        onClick = {
                            onDismissClick()
                            onQuitGameClick(openAndPopUp)
                        },
                        modifier = Modifier
                            .width(65.dp)
                            .background(color = Color.Red, shape = RoundedCornerShape(50.dp))
                            .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(50.dp)),
                        colors = ButtonDefaults.buttonColors(Color.Transparent),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "EXIT",
                            color = Color.White,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
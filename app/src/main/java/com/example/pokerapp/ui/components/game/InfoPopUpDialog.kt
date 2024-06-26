package com.example.pokerapp.ui.components.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun InfoPopUpDialog(
    titleText: String,
    descriptionText: String,
    buttonAction: () -> Unit,
    buttonText: String,
    isDismissable: Boolean,
    onDismiss: () -> Unit
){
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnClickOutside = isDismissable,
            dismissOnBackPress = isDismissable
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(8.dp)
                .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                        .background(Color(0xff4796d6))
                        .shadow(1.dp),
                ) {
                    Text(
                        text = titleText,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Text(
                    text = descriptionText,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                        .padding(horizontal = 35.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Box (
            modifier = Modifier
                .absoluteOffset(x = 130.dp, y = 140.dp)
        ) {
            Row {
                Button(
                    onClick = { buttonAction() },
                    modifier = Modifier
                        .width(75.dp)
                        .background(color = Color(0xffe03140), shape = RoundedCornerShape(50.dp))
                        .border(BorderStroke(1.dp, Color.White), RoundedCornerShape(50.dp)),
                    colors = ButtonDefaults.buttonColors(Color.Transparent),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = buttonText,
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
package com.example.pokerapp.ui.components.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ActionButton(
    buttonText: String,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit
){

    Button(
        onClick = { onButtonClick() },
        enabled = buttonEnabled,
        modifier = Modifier
            .width(
                if(buttonText == "RAISE" || buttonText == "CONFIRM")
                    110.dp
                else
                    90.dp
            )
            .alpha(
                if(buttonEnabled)
                    1f
                else
                    0f
            )
            .background(color = Color(0xffe03140), shape = RoundedCornerShape(10.dp))
            .border(BorderStroke(2.dp, Color(0xfffaf5ca)), RoundedCornerShape(10.dp)),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = PaddingValues(
            vertical = 0.dp,
            horizontal = 18.dp
        )
    ) {
        Text(
            text = buttonText,
            color = Color(0xfffaf5ca),
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Preview
@Composable
fun ActionButtonPreview(){
    ActionButton("CONFIRM", true, {})
}
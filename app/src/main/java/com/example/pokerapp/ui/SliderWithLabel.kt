package com.example.pokerapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SliderWithLabel(
    valueRange: ClosedFloatingPointRange<Float>,
    finiteEnd: Boolean,
    gameViewModel: GameViewModel
) {
    var sliderPosition by remember { mutableFloatStateOf(valueRange.start) }
    gameViewModel.raiseAmount = sliderPosition.toInt()

    val labelMinWidth: Dp = 55.dp

    Column (
        modifier = Modifier
            .rotate(270.0f)
            .width(310.dp)
            .absoluteOffset(y = 93.dp)
            .background(Color.DarkGray)
    ){
        BoxWithConstraints (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        ){
            val offset = getSliderOffset(
                value = sliderPosition,
                valueRange = valueRange,
                boxWidth = maxWidth,
                labelWidth = labelMinWidth + 8.dp
            )

            val endValueText = if (!finiteEnd && sliderPosition >= valueRange.endInclusive)
                "${sliderPosition.toInt()} +" else sliderPosition.toInt().toString()

            SliderLabel(
                label = endValueText,
                minWidth = labelMinWidth,
                modifier = Modifier
                    .padding(start = offset)
                    .rotate(90.0f)
            )

        }

        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = if(it >= valueRange.endInclusive){
                    valueRange.endInclusive
                } else {
                    ((it / valueRange.start).roundToInt() * valueRange.start)
                }
            },
            steps = (valueRange.endInclusive / valueRange.start).toInt() - 2,
            valueRange = valueRange,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xff961b1b),
                activeTrackColor = Color(0xffe33636)
            )
        )
    }

}

@Composable
fun SliderLabel(
    label: String,
    minWidth: Dp,
    modifier: Modifier = Modifier
) {
    Text(
        text = label,
        textAlign = TextAlign.Center,
        color = Color.White,
        modifier = modifier
            .background(
                color = Color(0xff961b1b),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .defaultMinSize(minWidth = minWidth)
    )
}

private fun getSliderOffset(
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    boxWidth: Dp,
    labelWidth: Dp
): Dp {
    val coerced = value.coerceIn(valueRange.start, valueRange.endInclusive)
    val positionFraction = calcFraction(valueRange.start, valueRange.endInclusive, coerced)

    return (boxWidth - labelWidth) * positionFraction
}

private fun calcFraction(a: Float, b: Float, pos: Float) =
    (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f,1f)
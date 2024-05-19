package com.example.pokerapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.pokerapp.R
import com.example.pokerapp.ui.theme.AccentColor
import com.example.pokerapp.ui.theme.BgColor
import com.example.pokerapp.ui.theme.Primary
import com.example.pokerapp.ui.theme.Secondary
import com.example.pokerapp.ui.theme.TextColor

@Composable
fun NormalTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Normal
        ),
        color = TextColor,
        textAlign = TextAlign.Center
    )
}

@Composable
fun HeadingTextComponent(value: String) {
    Text(
        text = value,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(),
        style = TextStyle(
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        ),
        color = TextColor,
        textAlign = TextAlign.Center
    )
}

@Composable
fun MyTextFieldComponent(labelValue: String, value: String, onNewValue: (String) -> Unit, icon: ImageVector) {
    OutlinedTextField(
        label = {
            Text(text = labelValue)
        },
        value = value,
        onValueChange = {
            onNewValue(it)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentColor,
            focusedLabelColor = AccentColor,
            cursorColor = Primary,
            focusedContainerColor = BgColor,
            focusedLeadingIconColor = AccentColor,
            focusedTextColor = TextColor
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions.Default
    )
}

@Composable
fun PasswordTextFieldComponent(labelValue: String, value: String, onNewValue: (String) -> Unit, icon: ImageVector) {

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val visibilityIcon =
        if(isPasswordVisible) painterResource(R.drawable.ic_visibility_on)
        else painterResource(R.drawable.ic_visibility_off)

    val visualTransformation =
        if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()

    OutlinedTextField(
        label = {
            Text(text = labelValue)
        },
        value = value,
        onValueChange = {
            onNewValue(it)
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = AccentColor,
            focusedLabelColor = AccentColor,
            cursorColor = Primary,
            focusedContainerColor = BgColor,
            focusedLeadingIconColor = AccentColor,
            focusedTextColor = TextColor
        ),
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "profile"
            )
        },
        trailingIcon = {
            IconButton(onClick = {
                isPasswordVisible = !isPasswordVisible
            }) {
                Icon(
                    painter = visibilityIcon,
                    contentDescription = "Visibility"
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation
    )
}

@Composable
fun BottomComponent(
    textQuery: String,
    textClickable: String,
    actionLabel: String,
    action: () -> Unit,
    changeAction: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = action,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = AccentColor,
                            shape = RoundedCornerShape(50.dp)
                        )
                        .fillMaxWidth()
                        .heightIn(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = actionLabel, color = Color.White, fontSize = 20.sp)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            AccountQueryComponent(textQuery, textClickable, changeAction)
        }
    }
}

@Composable
fun AccountQueryComponent(
    textQuery: String,
    textClickable: String,
    changeAction: () -> Unit
) {
    val annonatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = TextColor, fontSize = 15.sp)) {
            append(textQuery)
        }
        withStyle(style = SpanStyle(color = Secondary, fontSize = 15.sp)) {
            pushStringAnnotation(tag = textClickable, annotation = textClickable)
            append(textClickable)
        }
    }

    ClickableText(text = annonatedString, onClick = { changeAction() })
}
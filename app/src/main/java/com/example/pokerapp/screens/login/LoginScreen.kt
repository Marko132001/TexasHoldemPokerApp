package com.example.pokerapp.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokerapp.ui.components.BottomComponent
import com.example.pokerapp.ui.components.HeadingTextComponent
import com.example.pokerapp.ui.components.TextFieldComponent
import com.example.pokerapp.ui.components.NormalTextComponent
import com.example.pokerapp.ui.components.PasswordTextFieldComponent

@Composable
fun LoginScreen(
    context: Context,
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LoginScreenContent(
        context = context,
        errorMessage = viewModel.errorMessage.value,
        clearErrorMessage = viewModel::clearErrorMessage,
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogInClick = { viewModel.onLogInClick(openAndPopUp) },
        changeToSignUpScreen = { viewModel.changeToSignUpScreen(openAndPopUp) }
    )
}

@Composable
fun LoginScreenContent(
    context: Context,
    errorMessage: String?,
    clearErrorMessage: () -> Unit,
    uiState: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogInClick: () -> Unit,
    changeToSignUpScreen: () -> Unit
) {

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ) {
        LaunchedEffect(key1 = errorMessage) {
            errorMessage?.let{
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                clearErrorMessage()
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                NormalTextComponent(value = "Hey, there")
                HeadingTextComponent(value = "Welcome Back")
            }
            Spacer(modifier = Modifier.height(25.dp))
            Column {
                TextFieldComponent("Email", uiState.email, onEmailChange, Icons.Outlined.Email, imeAction = ImeAction.Next)
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent("Password", uiState.password, onPasswordChange, Icons.Outlined.Lock, imeAction = ImeAction.Done)
            }
            BottomComponent(
                textQuery = "Don't have an account? ",
                textClickable = "Register",
                actionLabel = "Login",
                action = { onLogInClick() },
                changeAction = { changeToSignUpScreen() }
            )
        }
    }
}
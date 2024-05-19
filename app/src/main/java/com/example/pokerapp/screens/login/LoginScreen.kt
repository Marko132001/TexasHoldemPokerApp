package com.example.pokerapp.screens.login

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokerapp.ui.components.BottomComponent
import com.example.pokerapp.ui.components.HeadingTextComponent
import com.example.pokerapp.ui.components.MyTextFieldComponent
import com.example.pokerapp.ui.components.NormalTextComponent
import com.example.pokerapp.ui.components.PasswordTextFieldComponent

@Composable
fun LoginScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    LoginScreenContent(uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogInClick = { viewModel.onLogInClick(openAndPopUp) },
        changeToSignUpScreen = { viewModel.changeToSignUpScreen(openAndPopUp) }
    )
}

@Composable
fun LoginScreenContent(
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
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                NormalTextComponent(value = "Hey, there")
                HeadingTextComponent(value = "Welcome Back")
            }
            Spacer(modifier = Modifier.height(25.dp))
            Column {
                MyTextFieldComponent("Email", uiState.email, onEmailChange, Icons.Outlined.Email)
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent("Password", uiState.password, onPasswordChange, Icons.Outlined.Lock)
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
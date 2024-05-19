package com.example.pokerapp.screens.sign_up

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
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
fun SignupScreen(
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    SignUpScreenContent(
        uiState = uiState,
        onUsernameChange = viewModel::onUsernameChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) },
        changeToLoginScreen = { viewModel.changeToLoginScreen(openAndPopUp) }
    )

}

@Composable
fun SignUpScreenContent(
    uiState: SignUpUiState,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit,
    changeToLoginScreen: () -> Unit
){
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
            NormalTextComponent(value = "Hello there,")
            HeadingTextComponent(value = "Create an Account")
            Spacer(modifier = Modifier.height(25.dp))

            Column {
                MyTextFieldComponent(
                    "Username",
                    uiState.username,
                    onUsernameChange,
                    Icons.Outlined.Person
                )
                Spacer(modifier = Modifier.height(10.dp))
                MyTextFieldComponent(
                    labelValue = "Email",
                    uiState.email,
                    onEmailChange,
                    icon = Icons.Outlined.Email
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent(
                    labelValue = "Password",
                    uiState.password,
                    onPasswordChange,
                    icon = Icons.Outlined.Lock
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent(
                    labelValue = "Repeat Password",
                    uiState.repeatPassword,
                    onRepeatPasswordChange,
                    icon = Icons.Outlined.Lock
                )
                BottomComponent(
                    textQuery = "Already have an account? ",
                    textClickable = "Login",
                    actionLabel = "Register",
                    action = { onSignUpClick() },
                    changeAction = { changeToLoginScreen() }
                )
            }
        }
    }
}
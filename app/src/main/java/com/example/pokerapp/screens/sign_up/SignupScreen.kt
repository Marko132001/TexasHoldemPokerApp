package com.example.pokerapp.screens.sign_up

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
import androidx.compose.material.icons.outlined.Person
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
fun SignupScreen(
    context: Context,
    openAndPopUp: (String, String) -> Unit,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    SignUpScreenContent(
        context = context,
        errorMessage = viewModel.errorMessage.value,
        clearErrorMessage = viewModel::clearErrorMessage,
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
    context: Context,
    errorMessage: String?,
    clearErrorMessage: () -> Unit,
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
        LaunchedEffect(key1 = errorMessage) {
            errorMessage?.let{
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                clearErrorMessage()
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            NormalTextComponent(value = "Hello there,")
            HeadingTextComponent(value = "Create an Account")
            Spacer(modifier = Modifier.height(25.dp))

            Column {
                TextFieldComponent(
                    "Username",
                    uiState.username,
                    onUsernameChange,
                    Icons.Outlined.Person,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(10.dp))
                TextFieldComponent(
                    labelValue = "Email",
                    uiState.email,
                    onEmailChange,
                    icon = Icons.Outlined.Email,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent(
                    labelValue = "Password",
                    uiState.password,
                    onPasswordChange,
                    icon = Icons.Outlined.Lock,
                    imeAction = ImeAction.Next
                )
                Spacer(modifier = Modifier.height(10.dp))
                PasswordTextFieldComponent(
                    labelValue = "Repeat Password",
                    uiState.repeatPassword,
                    onRepeatPasswordChange,
                    icon = Icons.Outlined.Lock,
                    imeAction = ImeAction.Done
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
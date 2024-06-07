package com.example.pokerapp.screens.login

import androidx.compose.runtime.mutableStateOf
import com.example.pokerapp.navigation.HOME_SCREEN
import com.example.pokerapp.navigation.LOGIN_SCREEN
import com.example.pokerapp.navigation.SIGN_UP_SCREEN
import com.example.pokerapp.common.ext.isValidEmail
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun changeToSignUpScreen(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(SIGN_UP_SCREEN, LOGIN_SCREEN)
        }
    }

    fun onLogInClick(openAndPopUp: (String, String) -> Unit) {
        if(!email.isValidEmail()) {
            _infoMessage.value = "Please insert a valid email."
            return
        }

        if(password.isBlank()){
            _infoMessage.value = "Password cannot be empty."
            return
        }

        launchCatching {
            try {
                accountService.authenticate(email, password)
                openAndPopUp(HOME_SCREEN, LOGIN_SCREEN)
            } catch (e: Exception) {
                _infoMessage.value = e.message
                return@launchCatching
            }
        }
    }
}
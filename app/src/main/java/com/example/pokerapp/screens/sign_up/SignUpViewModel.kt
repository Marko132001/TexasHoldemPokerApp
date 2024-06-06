package com.example.pokerapp.screens.sign_up

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.pokerapp.navigation.HOME_SCREEN
import com.example.pokerapp.navigation.LOGIN_SCREEN
import com.example.pokerapp.navigation.SIGN_UP_SCREEN
import com.example.pokerapp.common.ext.isValidEmail
import com.example.pokerapp.common.ext.isValidPassword
import com.example.pokerapp.common.ext.passwordMatches
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.screens.AppViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {
    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val username
        get() = uiState.value.username
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun changeToLoginScreen(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            openAndPopUp(LOGIN_SCREEN, SIGN_UP_SCREEN)
        }
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            _errorMessage.value = "Please insert a valid email."
            return
        }

        if (!password.isValidPassword()) {
            _errorMessage.value = "Your password should have at least six digits."
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            _errorMessage.value = "Passwords do not match."
            return
        }

        launchCatching {
            try {
                accountService.createAccount(email, password, username)
                openAndPopUp(HOME_SCREEN, SIGN_UP_SCREEN)
            }
            catch (e: Exception) {
                _errorMessage.value = e.message
                return@launchCatching
            }
        }
    }
}
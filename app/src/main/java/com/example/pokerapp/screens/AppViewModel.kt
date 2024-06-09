package com.example.pokerapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokerapp.model.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class AppViewModel() : ViewModel() {

    protected val _infoMessage = mutableStateOf<String?>(null)
    val infoMessage = _infoMessage

    protected val _userData = MutableStateFlow(UserData())
    val userData = _userData.asStateFlow()

    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            block = block
        )

    fun clearErrorMessage() {
        _infoMessage.value = null
    }
}
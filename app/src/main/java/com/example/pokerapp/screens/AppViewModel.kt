package com.example.pokerapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class AppViewModel() : ViewModel() {

    protected val _infoMessage = mutableStateOf<String?>(null)
    val infoMessage = _infoMessage
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            block = block
        )

    fun clearErrorMessage() {
        _infoMessage.value = null
    }
}
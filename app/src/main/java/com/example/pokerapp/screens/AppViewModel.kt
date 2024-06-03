package com.example.pokerapp.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class AppViewModel() : ViewModel() {

    protected val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage = _errorMessage
    fun launchCatching(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            block = block
        )

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
package com.example.pokerapp.screens.settings

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {

    var showProgressDialog by mutableStateOf(false)

    fun addImageToStorage(imageUri: Uri) {
        showProgressDialog = true
        launchCatching {
            try {
                _infoMessage.value = accountService.addImageToFirebaseStorage(imageUri)
            }
            catch (e: Exception) {
                _infoMessage.value = e.message
                return@launchCatching
            }
            finally {
                showProgressDialog = false
            }

        }
    }
}
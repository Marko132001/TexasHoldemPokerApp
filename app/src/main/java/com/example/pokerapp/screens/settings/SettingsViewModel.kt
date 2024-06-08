package com.example.pokerapp.screens.settings

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.pokerapp.model.firebase.AccountService
import com.example.pokerapp.navigation.HOME_SCREEN
import com.example.pokerapp.navigation.LOGIN_SCREEN
import com.example.pokerapp.navigation.SETTINGS_SCREEN
import com.example.pokerapp.screens.AppViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService
) : AppViewModel() {

    var showProgressDialog by mutableStateOf(false)

    init {
        launchCatching {
            updateUserData()
        }
    }

    fun onChangeProfilePictureClick(imageUri: Uri) {
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
                updateUserData()
            }

        }
    }

    fun onDeleteAccountClick(restartApp: (String) -> Unit){
        launchCatching {
            try {
                accountService.deleteAcount()
                restartApp(LOGIN_SCREEN)
            }
            catch (e: Exception){
                _infoMessage.value = e.message
                return@launchCatching
            }
        }
    }

    fun onChangeUsernameClick(newUsername: String){
        if(newUsername == ""){
            _infoMessage.value = "New username can't be blank."
            return
        }
        else if(newUsername == _userData.value.username){
            _infoMessage.value = "New username can't be the current username."
            return
        }

        launchCatching {
            try {
                _infoMessage.value = accountService.changeUsername(newUsername)
                updateUserData()
            }
            catch (e: Exception){
                _infoMessage.value = e.message
                return@launchCatching
            }
        }
    }

    fun onBackButtonClick(openAndPopUp: (String, String) -> Unit) {
        openAndPopUp(HOME_SCREEN, SETTINGS_SCREEN)
    }

    private suspend fun updateUserData(){
        accountService.currentUser.collect {
            _userData.value = it
        }
    }
}
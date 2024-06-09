package com.example.pokerapp.screens.settings

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.pokerapp.R
import com.example.pokerapp.model.UserData
import com.example.pokerapp.ui.components.common.TopNavigationBar
import com.example.pokerapp.ui.components.game.InfoPopUpDialog


@Composable
fun SettingsScreen(
    openAndPopUp: (String, String) -> Unit,
    restartApp: (String) -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
){

    val userData by settingsViewModel.userData.collectAsStateWithLifecycle()

    SettingsScreenContent(
        userData = userData,
        onChangeProfilePictureClick = settingsViewModel::onChangeProfilePictureClick,
        onChangeUsernameClick = settingsViewModel::onChangeUsernameClick,
        onDeleteAccountClick = { settingsViewModel.onDeleteAccountClick(restartApp) },
        onBackButtonClick = { settingsViewModel.onBackButtonClick(openAndPopUp) },
        infoMessage = settingsViewModel.infoMessage.value,
        clearErrorMessage = settingsViewModel::clearErrorMessage,
        showProgressDialog = settingsViewModel.showProgressDialog
    )
}


@Composable
fun SettingsScreenContent(
    userData: UserData,
    onChangeProfilePictureClick: (Uri) -> Unit,
    onChangeUsernameClick: (String) -> Unit,
    onDeleteAccountClick: () -> Unit,
    onBackButtonClick: () -> Unit,
    infoMessage: String?,
    clearErrorMessage: () -> Unit,
    showProgressDialog: Boolean
){
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    var username by remember { mutableStateOf("Username") }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }

    BackHandler(
        enabled = true,
        onBack = {
            if(showDeleteAccountDialog){
                showDeleteAccountDialog = false
            }
            else{
                onBackButtonClick()
            }
        }
    )

    Surface(
        color = Color(0xff1893b5),
        modifier = Modifier.fillMaxSize()
    ) {

        LaunchedEffect(key1 = userData) {
            username = userData.username
        }

        LaunchedEffect(key1 = infoMessage) {
            infoMessage?.let{
                Toast.makeText(context, infoMessage, Toast.LENGTH_SHORT).show()
                clearErrorMessage()
                imageUri = null
            }
        }

        if(showDeleteAccountDialog){
            InfoPopUpDialog (
                titleText = "Delete Account",
                descriptionText =
                    "Are you sure you want to delete your account? This action is irreversible!",
                buttonAction = {
                    showDeleteAccountDialog = false
                    onDeleteAccountClick()
                },
                buttonText = "DELETE",
                isDismissable = true,
                onDismiss = {
                    showDeleteAccountDialog = false
                }
            )
        }

        Scaffold(
            containerColor = Color(0xff1893b5),
            topBar = {
                TopNavigationBar(
                    titleText = "SETTINGS",
                    onBackButtonClick = onBackButtonClick
                )
            }
        ) { paddingValue ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValue)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val painter: Painter = if (imageUri != null) {
                    rememberAsyncImagePainter(imageUri)
                } else if (userData.avatarUrl == null) {
                    painterResource(id = R.drawable.unknown)
                } else {
                    rememberAsyncImagePainter(userData.avatarUrl)
                }

                Image(
                    painter = painter,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(150.dp)
                        .width(150.dp)
                        .border(
                            width = 2.dp,
                            color = Color.Black
                        )
                        .clickable {
                            launcher.launch("image/*")
                        }
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (imageUri != null) {
                            imageUri?.let { uri ->
                                onChangeProfilePictureClick(uri)
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Please select image from gallery",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .background(color = Color(0xffde7621), shape = RoundedCornerShape(5.dp))
                        .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(5.dp)),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "CHANGE PROFILE PICTURE",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 30.dp)
                )

                Spacer(modifier = Modifier.height(42.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, end = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Username:",
                        modifier = Modifier.width(140.dp),
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 25.sp
                    )
                    TextField(
                        value = username,
                        onValueChange = { username = it },
                        textStyle = TextStyle(fontSize = 22.sp),
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedTextColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedIndicatorColor = Color.White,
                            focusedIndicatorColor = Color.White
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        onChangeUsernameClick(username)
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .background(color = Color(0xff4796d6), shape = RoundedCornerShape(5.dp))
                        .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(5.dp)),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "CHANGE USERNAME",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Divider(
                    color = Color.Black,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 30.dp)
                )

                Spacer(modifier = Modifier.height(42.dp))

                Button(
                    onClick = {
                        showDeleteAccountDialog = true
                    },
                    modifier = Modifier
                        .wrapContentSize()
                        .background(color = Color.Red, shape = RoundedCornerShape(5.dp))
                        .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(5.dp)),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Text(
                        text = "DELETE ACCOUNT",
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        }

        if(showProgressDialog){
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 4.dp,
                modifier = Modifier
                    .wrapContentSize(Alignment.Center)
                    .size(50.dp)
            )
        }
    }
}
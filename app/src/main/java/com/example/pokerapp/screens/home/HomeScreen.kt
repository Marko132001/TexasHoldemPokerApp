package com.example.pokerapp.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pokerapp.screens.login.LoginScreenContent
import com.example.pokerapp.ui.components.game.PopUpDialog
import com.example.pokerapp.ui.theme.AccentColor

@Composable
fun HomeScreen(
    openAndPopUp: (String, String) -> Unit,
    openScreen: (String) -> Unit,
    restartApp: (String) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    HomeScreenContent(
        onPlayClick = { viewModel.onPlayClick(openAndPopUp) },
        onLeaderboardsClick = { viewModel.onLeaderboardsClick(openScreen) },
        onSettingsClick = { viewModel.onSettingsClick(openScreen) },
        onSignOutClick = { viewModel.onSignOutClick(restartApp) }
    )

}

@Composable
fun HomeScreenContent(
    onPlayClick: () -> Unit,
    onLeaderboardsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSignOutClick: () -> Unit
){

    var showDialog by remember { mutableStateOf(false) }

    if(showDialog){
        PopUpDialog(
            onPlayClick,
            onDismiss = { showDialog = false }
        )
    }

    Surface(
        color = Color(0xff1893b5),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Row (
            modifier = Modifier
                .padding(10.dp),
            horizontalArrangement = Arrangement.End
        ){
            Button(
                onClick = { onSignOutClick() },
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = Color.Red, shape = RoundedCornerShape(10.dp))
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(10.dp)),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
            ) {
                Icon(
                    Icons.Filled.ExitToApp,
                    null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "SIGN OUT",
                    color = Color.White,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = Color(0xffde7621), shape = RoundedCornerShape(50.dp))
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(Color.Transparent),
            ) {
                Text(
                    text = "PLAY",
                    color = Color.White,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { onLeaderboardsClick() },
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = Color(0xff4796d6), shape = RoundedCornerShape(50.dp))
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(
                    text = "LEADERBOARDS",
                    color = Color.White,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Button(
                onClick = { onSettingsClick() },
                modifier = Modifier
                    .wrapContentSize()
                    .background(color = Color(0xff4796d6), shape = RoundedCornerShape(50.dp))
                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Text(
                    text = "SETTINGS",
                    color = Color.White,
                    fontSize = 20.sp,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}
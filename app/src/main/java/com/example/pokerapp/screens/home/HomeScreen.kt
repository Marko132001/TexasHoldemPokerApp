package com.example.pokerapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pokerapp.R
import com.example.pokerapp.model.UserData
import com.example.pokerapp.ui.components.game.BuyInPopUpDialog
import com.example.pokerapp.ui.components.game.InfoPopUpDialog

@Composable
fun HomeScreen(
    openAndPopUp: (String, String) -> Unit,
    openScreen: (String) -> Unit,
    restartApp: (String) -> Unit,
    homeViewModel: HomeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
) {

    val userData by homeViewModel.userData.collectAsStateWithLifecycle()

    HomeScreenContent(
        minBuyIn = homeViewModel.minBuyIn,
        maxBuyIn = homeViewModel.maxBuyIn,
        userData = userData,
        openAndPopUp = openAndPopUp,
        onPlayClick = homeViewModel::onPlayClick,
        onLeaderboardsClick = { homeViewModel.onLeaderboardsClick(openScreen) },
        onSettingsClick = { homeViewModel.onSettingsClick(openAndPopUp) },
        onSignOutClick = { homeViewModel.onSignOutClick(restartApp) }
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    minBuyIn: Int,
    maxBuyIn: Int,
    userData: UserData,
    openAndPopUp: (String, String) -> Unit,
    onPlayClick: ((String, String) -> Unit, Int) -> Unit,
    onLeaderboardsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onSignOutClick: () -> Unit
){

    var showDialog by remember { mutableStateOf(false) }
    var showSignOutDialog by remember { mutableStateOf(false) }

    if(showSignOutDialog){
        InfoPopUpDialog (
            titleText = "Sign Out",
            descriptionText = "Are you sure you want to sign out?",
            buttonAction = {
                showSignOutDialog = false
                onSignOutClick()
            },
            buttonText = "SIGN OUT",
            isDismissable = true,
            onDismiss = {
                showSignOutDialog = false
            }
        )
    }

    if(showDialog){
        BuyInPopUpDialog(
            minBuyIn = minBuyIn,
            maxBuyIn = maxBuyIn,
            userChips = userData.chipAmount,
            openAndPopUp = openAndPopUp,
            onPlayClick = onPlayClick,
            userDismissEnabled = true,
            onQuitGameClick = {},
            onDismissClick = { showDialog = false }
        )
    }

    Surface(
        color = Color(0xff1893b5),
        modifier = Modifier
            .fillMaxSize()
    ) {
        Scaffold(
            containerColor = Color(0xff1893b5),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xff10556e),
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            if (userData.avatarUrl == null) {
                                Image(
                                    modifier = Modifier
                                        .size(55.dp)
                                        .border(
                                            BorderStroke(2.dp, Color(0xffffe6a1)),
                                            CircleShape
                                        )
                                        .clip(CircleShape),
                                    painter = painterResource(R.drawable.unknown),
                                    contentDescription = null,
                                    alignment = Alignment.Center,
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(55.dp)
                                        .border(
                                            BorderStroke(2.dp, Color(0xffffe6a1)),
                                            CircleShape
                                        )
                                        .clip(CircleShape),
                                    model = userData.avatarUrl,
                                    contentDescription = null,
                                    alignment = Alignment.Center,
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Column(
                                modifier = Modifier.padding(start = 10.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    modifier = Modifier,
                                    text = userData.username,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xffffe6a1),
                                    fontSize = 22.sp
                                )
                                Row(
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Image(
                                        modifier = Modifier
                                            .size(32.dp),
                                        painter = painterResource(R.drawable.chip_stack),
                                        contentDescription = null,
                                        alignment = Alignment.Center,
                                        contentScale = ContentScale.Crop
                                    )
                                    Text(
                                        modifier = Modifier,
                                        text = "$${userData.chipAmount}",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xffffe6a1),
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        Row(
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Button(
                                onClick = { showSignOutDialog = true },
                                modifier = Modifier
                                    .wrapContentSize()
                                    .background(color = Color.Red, shape = RoundedCornerShape(10.dp))
                                    .border(BorderStroke(2.dp, Color.White), RoundedCornerShape(10.dp)),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Icon(
                                    Icons.Filled.ExitToApp,
                                    null
                                )
                            }
                        }
                    }
                )
            }
        ) {}

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { showDialog = true },
                enabled = userData.chipAmount >= minBuyIn,
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
                    text = "LEADERBOARD",
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
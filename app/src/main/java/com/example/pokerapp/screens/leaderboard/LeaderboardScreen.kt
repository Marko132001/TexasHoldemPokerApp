package com.example.pokerapp.screens.leaderboard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pokerapp.model.UserData
import com.example.pokerapp.ui.components.common.TopNavigationBar
import com.example.pokerapp.ui.components.leaderboard.UserItem

@Composable
fun LeaderboardScreen(
    openAndPopUp: (String, String) -> Unit,
    leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
){

    val users = leaderboardViewModel.users.collectAsStateWithLifecycle(emptyList())

    LeaderboardScreenContent(
        users = users.value,
        onBackButtonClick = { leaderboardViewModel.onBackButtonClick(openAndPopUp) },
    )

}

@Composable
fun LeaderboardScreenContent(
    users: List<UserData>,
    onBackButtonClick: () -> Unit
){

    Surface(
        color = Color(0xff1893b5),
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(
            containerColor = Color(0xff1893b5),
            topBar = {
                TopNavigationBar(
                    titleText = "LEADERBOARD",
                    onBackButtonClick = onBackButtonClick
                )
            }
        ) { paddingValue ->

            LazyColumn(
                modifier = Modifier.padding(paddingValue)
            ) {
                itemsIndexed(users){index, userData ->
                    UserItem(
                        userPosition = index+1,
                        userData = userData
                    )
                    Divider(color = Color.Black, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 10.dp))
                }
            }
        }
    }
}
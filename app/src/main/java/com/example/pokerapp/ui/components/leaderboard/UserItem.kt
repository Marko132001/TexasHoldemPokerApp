package com.example.pokerapp.ui.components.leaderboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.pokerapp.R
import com.example.pokerapp.model.UserData

@Composable
fun UserItem(
    userPosition: Int,
    userData: UserData
){

    val painter: Painter = if (userData.avatarUrl != null) {
        rememberAsyncImagePainter(userData.avatarUrl)
    } else {
        painterResource(id = R.drawable.unknown)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .height(70.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.6f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier.width(50.dp),
                text = "$userPosition.",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xffffe6a1),
                fontSize = 23.sp
            )
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                modifier = Modifier
                    .size(43.dp)
                    .border(
                        BorderStroke(2.dp, Color(0xffffe6a1)),
                        CircleShape
                    )
                    .clip(CircleShape),
                painter = painter,
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                modifier = Modifier,
                text = userData.username,
                fontWeight = FontWeight.Bold,
                color = Color(0xffffe6a1),
                fontSize = 20.sp
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(0.7f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(22.dp),
                painter = painterResource(R.drawable.pot_chip),
                contentDescription = null,
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                modifier = Modifier,
                text = "$${userData.chipAmount}",
                fontWeight = FontWeight.Bold,
                color = Color(0xffffe6a1),
                fontSize = 17.sp
            )
        }
    }
}

@Preview
@Composable
fun UserItemPreview(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xff10556e))
    ) {
        UserItem(
            userPosition = 1,
            UserData("dsfg344343", "Marko1234", 5000000, null)
        )
        Divider(color = Color.Black, thickness = 1.dp, modifier = Modifier.padding(vertical = 10.dp))
        UserItem(
            userPosition = 120,
            UserData("dsfg344343", "Marko", 3000000, null)
        )
    }
}
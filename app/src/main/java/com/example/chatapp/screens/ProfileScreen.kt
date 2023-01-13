package com.example.chatapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chatapp.models.UserModel
import com.example.chatapp.R
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun ProfileScreen(userModel: UserModel) {
    Scaffold(topBar = { TopAppBar(title = { Text(text = "Profile") }) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Column(
                    modifier = Modifier
                        .background(
                            color = Color(0xAA760094).copy(alpha = 0.1f),
                            shape = CircleShape
                        )
                        .size(96.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        model = userModel.profileImage,
                        placeholder = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                        error = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                        contentDescription = ""
                    )
                }
            }

            Text(text = userModel.bio)

            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null,
                    tint = Color(0xFF760094)
                )
                Text(text = userModel.email, modifier = Modifier.padding(start = 8.dp))
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = Color(0xFF760094)
                )
                Text(text = userModel.dateOfBirth, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        ProfileScreen()
    }
}


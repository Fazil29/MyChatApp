package com.example.chatapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.Util.Screen
import com.example.chatapp.Util.Widgets.ChatListItem
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text(text = "Home") }, actions = {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_person_24),
            contentDescription = "Edit Profile Button",
            modifier = Modifier.clickable {
                navController.navigate(Screen.Profile.route)
            }
        )
    }) }) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn() {
                items(count = 20) {
                    ChatListItem()
                }
            }

            FloatingActionButton(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .padding(end = 16.dp, bottom = 96.dp)
                    .align(Alignment.BottomEnd),
                backgroundColor = Color(0xFF760094)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_people_24),
                    contentDescription = null
                )
            }
            FloatingActionButton(
                onClick = { /*TODO*/ }, modifier = Modifier
                    .padding(end = 16.dp, bottom = 24.dp)
                    .align(Alignment.BottomEnd),
                backgroundColor = Color(0xFF760094)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_add_24),
                    contentDescription = null
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        HomeScreen()
    }
}
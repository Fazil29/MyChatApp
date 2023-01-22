package com.example.chatapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chatapp.R
import com.example.chatapp.data.models.ChannelModel
import com.example.chatapp.util.Screen
import com.example.chatapp.util.widgets.ChatListItem
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.util.Response

@Composable
fun HomeScreen(
    navController: NavController,
    ourId: String,
    ourInteractedChannels: Response<List<ChannelModel>>,
    getOurInteractedChannels: (String) -> Unit
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Home") },
            actions = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_24),
                    contentDescription = "Edit Profile Button",
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.EditProfile.route)
                    }
                )
            })
    }) {
        LaunchedEffect(key1 = Unit){
            getOurInteractedChannels(ourId)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when(ourInteractedChannels){
                is Response.SUCCESS -> {
                    LazyColumn {
                        items(ourInteractedChannels.data) { channel ->
                            ChatListItem(navController, channel)
                        }
                    }
                }
                is Response.ERROR -> {
                    Column(modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Button(onClick = { getOurInteractedChannels(ourId) }) {
                            Text(text = ourInteractedChannels.message)
                        }
                    }
                }
                is Response.LOADING -> {
                    Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center) {
                        CircularProgressIndicator()
                    }
                }
            }

            FloatingActionButton(
                onClick = { navController.navigate(Screen.CreateNewGroupScreen.route) },
                modifier = Modifier
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
                onClick = { navController.navigate(Screen.NewChat.route) }, modifier = Modifier
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
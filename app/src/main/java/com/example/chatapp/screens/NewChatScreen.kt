package com.example.chatapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.chatapp.data.models.UserModel
import com.example.chatapp.util.widgets.NewChatListItem
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.util.Response
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

@Composable
fun NewChatScreen(
    ourUser: UserModel,
    navController: NavController,
    getUsers: (String) -> Unit,
    userList: Response<List<UserModel>>,
    setViewingUser: KFunction1<UserModel, Unit>,
    getChannelId: KSuspendFunction1<List<UserModel>, String>
) {
    Scaffold(topBar = { TopAppBar(title = { Text(text = "New Chat") }) }) {
        LaunchedEffect(key1 = Unit) {
            getUsers(ourUser.id)
        }

        when (userList) {
            is Response.SUCCESS -> {
                LazyColumn() {
                    items(userList.data) { user ->
                        NewChatListItem(
                            navController,
                            user,
                            setViewingUser,
                            getChannelId,
                            listOf(ourUser, user)
                        )
                    }
                }
            }
            is Response.ERROR -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { getUsers(ourUser.id) }) {
                        Text(text = "Retry")
                    }
                }
            }
            is Response.LOADING -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        NewChatScreen(channelViewModel::getUsers)
    }
}
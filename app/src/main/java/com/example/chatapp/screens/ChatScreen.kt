package com.example.chatapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatScreen(channelId: String) {
    var message by remember {
        mutableStateOf("")
    }
    Scaffold(topBar = { TopAppBar(title = { Text(text = "Chat") }) }) {
        Column(modifier = Modifier.padding(16.dp)) {

            LazyColumn(
                Modifier
                    .weight(11f)
                    .fillMaxSize()
            ) {
                items(count = 50) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(text = channelId)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(text = channelId)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(modifier = Modifier.fillMaxSize(),
                    value = message,
                    onValueChange = {
                        message = it
                    }, placeholder = {
                        Text(text = "message")
                    }, trailingIcon = {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Create, contentDescription = null)
                    }
                )
            }
        }
    }
}
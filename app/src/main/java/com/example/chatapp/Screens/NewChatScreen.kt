package com.example.chatapp.Screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.chatapp.Util.Widgets.NewChatListItem
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun NewChatScreen() {
    LazyColumn() {
        items(count = 20) {
            NewChatListItem()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
        NewChatScreen()
    }
}
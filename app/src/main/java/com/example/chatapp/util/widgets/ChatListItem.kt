package com.example.chatapp.util.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatapp.R
import com.example.chatapp.ui.theme.ChatAppTheme

@Composable
fun ChatListItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_person_24),
            contentDescription = "Profile Picture",
            modifier = Modifier.weight(0.2f)
        )
        Column(modifier = Modifier.weight(0.6f)) {
            Text(text = "Lorem Ipsum")
            Text(text = "Lorem Ipsum")
        }
        Text(text = "10:00 AM", modifier = Modifier.weight(0.2f))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
        ChatListItem()
    }
}
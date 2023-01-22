package com.example.chatapp.util.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.data.models.ChannelModel
import com.example.chatapp.data.models.ChannelType
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.util.Screen
import kotlinx.coroutines.launch

@Composable
fun ChatListItem(navController: NavController, channel: ChannelModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .weight(0.2f),
            model = channel.profilePic,
            placeholder = painterResource(id = R.drawable.ic_baseline_person_24),
            error = painterResource(id = R.drawable.ic_baseline_person_24),
            contentDescription = "Profile Picture",
        )
        Column(modifier = Modifier
            .weight(0.6f)
            .clickable {
                when (channel.type) {
                    ChannelType.OneToOne -> {
                        navController.navigate("${Screen.ChatScreen.route}/${channel.id}")
                    }
                    ChannelType.Group -> {
                        navController.navigate("${Screen.GroupChatScreen.route}/${channel.id}")
                    }
                }
            }) {
            Text(text = channel.name)
            Text(text = "Last Message")
        }
        Text(text = "10:00 AM", modifier = Modifier.weight(0.2f))
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        ChatListItem()
    }
}
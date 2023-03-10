package com.example.chatapp.util.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.data.models.UserModel
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.util.Screen
import kotlinx.coroutines.launch
import kotlin.reflect.KFunction1
import kotlin.reflect.KSuspendFunction1

@Composable
fun NewChatListItem(
    navController: NavController,
    userModel: UserModel,
    setViewingUser: KFunction1<UserModel, Unit>,
    getChannelId: KSuspendFunction1<List<UserModel>, String>,
    members: List<UserModel>
) {
    val scope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clickable {
                    setViewingUser(userModel)
                    navController.navigate(Screen.Profile.route)
                }
                .weight(0.2f),
            model = userModel.profileImage,
            placeholder = painterResource(id = R.drawable.ic_baseline_person_24),
            error = painterResource(id = R.drawable.ic_baseline_person_24),
            contentDescription = "Profile Picture",
        )
        Column(modifier = Modifier
            .weight(0.8f)
            .clickable {
                scope.launch {
                    val channelId = getChannelId(members)
                    navController.navigate("${Screen.ChatScreen.route}/$channelId")
                }
            }) {
            Text(text = userModel.name)
            Text(text = userModel.id)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        NewChatListItem()
    }
}
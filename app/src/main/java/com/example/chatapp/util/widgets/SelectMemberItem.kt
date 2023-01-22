package com.example.chatapp.util.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

@Composable
fun SelectMemberItem(
    navController: NavController,
    user: UserModel,
    setViewingUser: (UserModel) -> Unit,
    selectMember: (Boolean) -> Unit
) {

    var isSelected by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp)
            .padding(16.dp)
            .clickable {
                isSelected = !isSelected
                selectMember(isSelected)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .clickable {
                    setViewingUser(user)
                    navController.navigate(Screen.Profile.route)
                }
                .weight(0.2f),
            model = user.profileImage,
            placeholder = painterResource(id = R.drawable.ic_baseline_person_24),
            error = painterResource(id = R.drawable.ic_baseline_person_24),
            contentDescription = "Profile Picture",
        )
        Column(modifier = Modifier.weight(0.6f)) {
            Text(text = user.name)
            Text(text = user.id)
        }
        Checkbox(checked = isSelected, onCheckedChange = null)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        SelectMemberItem()
    }
}
package com.example.chatapp.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.chatapp.R
import com.example.chatapp.data.models.ChannelModel
import com.example.chatapp.data.models.UserModel
import com.example.chatapp.ui.theme.ChatAppTheme
import com.example.chatapp.util.Response
import com.example.chatapp.util.Screen
import com.example.chatapp.util.widgets.SelectMemberItem
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.coroutines.launch

@Composable
fun CreateNewGroupScreen(
    con: Activity,
    navController: NavController,
    users: Response<List<UserModel>>,
    getAllUsers: (String) -> Unit,
    ourId: String,
    setViewingUser: (UserModel) -> Unit,
    createGroupChannel: suspend (ChannelModel) -> String
) {
    var profileImage by remember {
        mutableStateOf("")
    }

    var groupName by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    val members = remember {
        mutableStateListOf(ourId)
    }

    var isEnabled by remember {
        mutableStateOf(false)
    }

    isEnabled =
        (profileImage.isNotEmpty() && groupName.isNotEmpty() && description.isNotEmpty() && members.size > 1)

    val imageSelectLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                profileImage = result.data?.data.toString()
            }
        }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        getAllUsers(ourId)
    }

    Scaffold(topBar = { TopAppBar(title = { Text(text = "Create New Group") }) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            .clip(CircleShape)
                            .clickable {
                                ImagePicker
                                    .with(con)
                                    .cropSquare()
                                    .compress(256)
                                    .createIntent { intent ->
                                        imageSelectLauncher.launch(intent)
                                    }
                            },
                        model = profileImage,
                        placeholder = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                        error = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                        contentDescription = ""
                    )
                    Text(text = "Add Image", color = Color(0xAA760094))
                }
            }

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = groupName,
                onValueChange = {
                    groupName = it
                },
                label = { Text(text = "Group Name") },
                placeholder = {
                    Text(
                        text = "Group Name"
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_person_24),
                        contentDescription = ""
                    )
                }
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = description,
                onValueChange = {
                    description = it
                },
                label = { Text(text = "Group Description") },
                placeholder = {
                    Text(
                        text = "Group Description"
                    )
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_baseline_info_24),
                        contentDescription = ""
                    )
                },
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Members",
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Bold
            )

            Column(modifier = Modifier.weight(4f)) {
                when (users) {
                    is Response.SUCCESS -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(users.data) { user ->
                                SelectMemberItem(navController, user, setViewingUser) {
                                    if (it)
                                        members.add(user.id)
                                    else
                                        members.remove(user.id)
                                }
                            }
                        }
                    }
                    is Response.ERROR -> {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(onClick = { getAllUsers(ourId) }) {
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp), horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = {
                        scope.launch {
                            val channelId = createGroupChannel(
                                ChannelModel(
                                    profilePic = profileImage,
                                    name = groupName,
                                    description = description,
                                    member = members
                                )
                            )
                            navController.navigate("${Screen.GroupChatScreen.route}/$channelId") {
                                popUpTo(Screen.Home.route)
                            }
                        }
                    },
                    enabled = isEnabled
                ) {
                    Text(text = "Create")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ChatAppTheme {
//        CreateNewGroupScreen()
    }
}
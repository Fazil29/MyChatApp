package com.example.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatapp.Screens.*
import com.example.chatapp.Util.Screen
import com.example.chatapp.ViewModels.MainViewModel
import com.example.chatapp.ui.theme.ChatAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity @Inject constructor() : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.getCredentialsFromLocal()
        setContent {
            ChatAppTheme {
                val controller = rememberNavController()
                var topBarTitle by remember {
                    mutableStateOf("")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    backgroundColor = MaterialTheme.colors.background
                ) {
                    NavHost(navController = controller, startDestination = Screen.Splash.route) {
                        composable(route = Screen.Splash.route) {
                            SplashScreen(
                                controller,
                                mainViewModel.credentialsFromLocal.value
                            )
                        }
                        composable(route = Screen.Login.route) {
                            LoginScreen(
                                controller,
                                mainViewModel::openDialog,
                                mainViewModel::signIn
                            )
                        }
                        composable(route = Screen.Home.route) {
                            HomeScreen(controller)
                        }
                        composable(route = Screen.EditProfile.route) {
                            EditProfileScreen(controller, this@MainActivity, mainViewModel.user, mainViewModel::saveCredentials, mainViewModel::saveProfilePicToFirebase)
                        }
                        composable(route = Screen.Profile.route) {
                            ProfileScreen(mainViewModel.user)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChatAppTheme {
        Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
            TopAppBar() {
                Text(text = "Profile")
            }
        }, backgroundColor = MaterialTheme.colors.background) {
        }
    }
}
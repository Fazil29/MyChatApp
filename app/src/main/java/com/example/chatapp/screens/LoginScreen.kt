package com.example.chatapp.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.chatapp.util.Screen
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2

@Composable
fun LoginScreen(
    navController: NavController,
    openDialog: (ActivityResultLauncher<IntentSenderRequest>) -> Unit,
    signIn: KSuspendFunction2<ActivityResult, (String) -> Unit, Unit>
) {

    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            scope.launch {
                signIn(it) {
                    navController.navigate(route = it) {
                        this.popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }
            }
        }

    Scaffold(topBar = { TopAppBar(title = { Text(text = "Login") }) }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA760094).copy(alpha = 0.1f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { openDialog.invoke(launcher) }) {
                Text(text = "Sign In With Google")
            }
        }
    }
}
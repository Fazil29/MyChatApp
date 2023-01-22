package com.example.chatapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.chatapp.data.models.UserModel
import com.example.chatapp.R
import com.example.chatapp.util.Response
import com.example.chatapp.util.Screen
import kotlin.reflect.KFunction0

@Composable
fun SplashScreen(
    navController: NavController,
    getCredentials: KFunction0<Unit>,
    credentials: Response<UserModel?>
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF150099),
                        Color(0xFF6E008A)
                    )
                )
            ), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.vector),
            contentDescription = "App Icon"
        )

        LaunchedEffect(Unit) {
            getCredentials()
        }


        when (credentials) {
            is Response.SUCCESS -> {
                credentials.data?.let {
                    LaunchedEffect(Unit) {
                        navController.navigate(Screen.Home.route) {
                            this.popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }
            is Response.ERROR -> {
                LaunchedEffect(Unit) {
                    Log.d("TAG", "${credentials.message}")
                    navController.navigate(Screen.Login.route) {
                        this.popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                }
            }
            is Response.LOADING -> {
            }
        }
    }
}
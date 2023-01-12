package com.example.chatapp.util

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object EditProfile : Screen("edit_profile_screen")
    object Profile : Screen("profile_screen")

}
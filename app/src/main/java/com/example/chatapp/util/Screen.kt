package com.example.chatapp.util

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object EditProfile : Screen("edit_profile_screen")
    object Profile : Screen("profile_screen")
    object NewChat : Screen("new_chat")
    object NewGroupChat : Screen("new_group_chat")
    object ChatScreen : Screen("chat_screen")
    object CreateNewGroupScreen : Screen("create_new_group_screen")
    object GroupChatScreen : Screen("group_chat_screen")

}
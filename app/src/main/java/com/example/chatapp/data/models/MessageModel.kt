package com.example.chatapp.data.models

import com.google.firebase.Timestamp

data class MessageModel(
    val time: Timestamp,
    val sender: String,
    val message: String,
    val mediaUrl: String?,
)

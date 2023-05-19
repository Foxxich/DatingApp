package com.example.datingapp.chat

import com.google.firebase.Timestamp

data class Message(
    val text: String = "",
    val sender: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
package com.example.datingapp.chat

data class Chat(
    val userId: String = "",
    val messagesList: MutableList<Message> = mutableListOf(),
)

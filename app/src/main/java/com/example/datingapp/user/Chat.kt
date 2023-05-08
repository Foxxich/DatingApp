package com.example.datingapp.user

data class Chat(
    val userId: String = "",
    val messagesList: MutableList<Message> = mutableListOf(),
)

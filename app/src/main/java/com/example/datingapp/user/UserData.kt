package com.example.datingapp.user

import com.example.datingapp.chat.Chat

data class UserData(
    val userId: String = "",
    val userName: String = "",
    val quantityUserMatchedWith: Int = 0,
    val matchedWith: MutableList<String> = mutableListOf(),
    val chats: MutableList<Chat> = mutableListOf(),
    val swiped: MutableMap<String, Boolean> = mutableMapOf(),
    val interests: List<Interest> = emptyList(),
)
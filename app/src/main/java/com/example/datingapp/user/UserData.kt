package com.example.datingapp.user

import com.example.datingapp.chat.Chat

data class UserData(
    val userId: String = "",
    var userName: String = "",
    val quantityUserMatchedWith: Int = 0,
    val matchedWith: MutableList<String> = mutableListOf(),
    val chats: MutableList<Chat> = mutableListOf(),
    val swiped: MutableMap<String, Boolean> = mutableMapOf(),
    var interests: List<Interest> = emptyList(),
)
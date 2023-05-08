package com.example.datingapp.user

data class UserData(
    val userId: String = "",
    val userName: String = "",
    val quantityUserMatchedWith: Int = 0,
    val matchedWith: List<String> = emptyList(),
    val chats: MutableList<Chat> = mutableListOf(),
    val swiped: MutableMap<String, Boolean> = mutableMapOf(),
    val interests: List<Interest> = emptyList(),
)
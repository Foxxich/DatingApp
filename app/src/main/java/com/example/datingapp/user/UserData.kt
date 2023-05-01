package com.example.datingapp.user

data class UserData(
    val userId: String,
    val userName: String,
    val quantityUserMatchedWith: Int = 0,
    val userMatchedWithList: List<String> = emptyList(),
    val interests: MutableList<Interest>
)
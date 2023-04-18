package com.example.datingapp.user

import android.media.Image

data class UserData(
    val userId: Int,
    val userName: String,
    val userAge: Int,
    val quantityUserMatchedWith: Int,
    val userMatchedWithList: MutableList<Int>,
    val interests: MutableList<Interest>,
    var userImage: Image?
)
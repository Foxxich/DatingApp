package com.example.datingapp.user

import android.net.Uri

data class UserDataCollection(
    var userPhoto: Uri?,
    var userData: UserData,
    var notSwipedUsersUri: MutableMap<UserData, Uri> = mutableMapOf(),
    var matchedWithUsersUri: MutableMap<UserData, Uri> = mutableMapOf(),
)

package com.example.datingapp.user

import android.net.Uri

data class UserCollection(
    var userPhoto: Uri?,
    var userData: UserData,
    var notSwipedUsersUri: MutableMap<UserData, Uri>,
    var matchedWithUsersUri: MutableMap<UserData, Uri>
)

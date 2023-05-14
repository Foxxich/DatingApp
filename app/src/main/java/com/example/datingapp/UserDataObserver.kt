package com.example.datingapp

import com.example.datingapp.user.UserData

interface UserDataObserver {
    fun dataChanged(userData: UserData)
}
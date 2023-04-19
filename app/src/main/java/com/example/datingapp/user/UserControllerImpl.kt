package com.example.datingapp.user

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserControllerImpl @Inject constructor() : UserController {

    private lateinit var testVal: String

    override fun addStandardUserData(test: String) {
        testVal = test
    }

    override fun getData(): String {
        return testVal
    }
}
package com.example.datingapp.data

import com.example.datingapp.user.UserData

internal class UserParameters {

    companion object {
        const val CORRECT_PASSWORD = "123456"
        const val CORRECT_EMAIL = "user7@gmail.com"
        const val INCORRECT_EMAIL = "random@gmail.com"
        const val TEST_EMAIL = "test1@gmail.com"
        const val INCORRECT_PASSWORD = "xdxdxd"
        const val CORRECT_UID = "4GiuevU4c3dJXJaL2GzrY6aQEBn2"
        const val CHAT_UID = "gl0FJ4Sv3fbHYpoTCa8U8UHMrxQ2"

        var TEST_USER_DATA = UserData(
            userId = "testId",
            userName = "testName",
            quantityUserMatchedWith = 0
        )
    }

}
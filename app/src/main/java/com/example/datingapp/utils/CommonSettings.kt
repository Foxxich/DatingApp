package com.example.datingapp.utils

import android.content.Context
import android.content.Intent
import com.example.datingapp.activities.ConnectionLostActivity

object CommonSettings {
    const val TAG = "DATING_APP"

    fun showConnectionLost(context: Context) {
        val intent =
            Intent(context, ConnectionLostActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}
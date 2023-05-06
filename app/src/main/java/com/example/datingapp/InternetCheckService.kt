package com.example.datingapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.datingapp.utils.CommonSettings.TAG

const val NOTIFICATION_CHANNEL_ID = "InternetConnectionWarningChannel"
const val NOTIFICATION_ID = 1


class InternetCheckService : Service() {
    private val connectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val notification by lazy {
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("No Internet Connection")
            .setContentText("Please connect to the internet to continue using this app.")
            .setSmallIcon(R.drawable.heart_icon)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        checkInternetConnection()
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_DETACH)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Internet Connection Warning",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun checkInternetConnection() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                // The network is available
                Log.d(TAG, "onAvailable")
                notificationManager.cancel(NOTIFICATION_ID)
            }

            override fun onLost(network: Network) {
                // The network is lost
                Log.d(TAG, "onLost")
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
        }

        // Register the network callback
        val networkRequest = NetworkRequest.Builder().build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }
}
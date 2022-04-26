package com.gdsc.lineup.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.gdsc.lineup.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/**
 * Created by Nakul
 * on 26,April,2022
 */
@AndroidEntryPoint
class LocationService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "LINE_UP"
        var serviceRunning = false
    }

    override fun onCreate() {
        super.onCreate()
        serviceRunning = true

        SocketHelper.init()

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.drawable.ic_launcher_background)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_ID, NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = NOTIFICATION_CHANNEL_ID
            notificationChannel.setSound(null, null)
            notificationManager.createNotificationChannel(notificationChannel)
            startForeground(1, builder.build())
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startPinging()
        return START_STICKY
    }

    private fun startPinging() = flow {
        while (true) {
            updateLocation()
            emit(Unit)
            delay(3000L)
        }
    }

    private fun updateLocation() {
//        val locationRequest = LocationRequest.create().apply {
//
//        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceRunning = false
    }
}
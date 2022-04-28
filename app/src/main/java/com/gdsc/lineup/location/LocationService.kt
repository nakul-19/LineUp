package com.gdsc.lineup.location

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.gdsc.lineup.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.socket.emitter.Emitter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.json.JSONException
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Nakul
 * on 26,April,2022
 */
@AndroidEntryPoint
class LocationService : Service() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "LINE_UP"
        var serviceRunning = false
        var lastLocation: Location? = null
    }

    @Inject
    lateinit var sp: SharedPreferences

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val listener = Emitter.Listener {
//        val data = Gson().fromJson(it[0]?.toString(), SocketDataModel::class.java)/*Gson().fromJson(it[0]?.toString(), MessageModel::class.java)*/
        Timber.d("ReceivedMessage:\n${it[0]?.toString()}")
        try {
        } catch (e: JSONException) {
            Timber.e(e)
        }

    }

    override fun onCreate() {
        super.onCreate()
        serviceRunning = true

        SocketHelper.init()
        SocketHelper.collect(listener)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setOngoing(false)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)

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
        Timber.d("Start")
        scope.launch {
            startPinging().collect {}
        }
        return START_STICKY
    }

    private fun startPinging() = flow {
        while (true) {
            Timber.d("Flowing")
            updateLocation()
            emit(Unit)
            delay(3000L)
        }
    }

    private fun updateLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Timber.e("Location not granted")
            return
        }

        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(2)
            fastestInterval = TimeUnit.SECONDS.toMillis(3)
            maxWaitTime = TimeUnit.SECONDS.toMillis(3)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val locationClient = LocationServices.getFusedLocationProviderClient(this)

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                Timber.d("Location: ${result.lastLocation}")
                if (lastLocation == result.lastLocation || !sp.contains("zealId"))
                    return
                lastLocation = result.lastLocation
                val pingModel = MessageModel(
                    sp.getString("zealId", "") ?: "",
                    sp.getString("avatar", "") ?: "",
                    lastLocation!!.longitude,
                    lastLocation!!.latitude
                )
                SocketHelper.send(
                    SocketDataModel(
                        sp.getString("teamId", "123") ?: "123",
                        pingModel
                    )
                )
            }
        }

        try {
            Timber.d("requestLocationUpdates")
            locationClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceRunning = false
        SocketHelper.stopCollection(listener)
        SocketHelper.disconnect()
        job.cancel()
    }

}
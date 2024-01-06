package com.devYoussef.nb3elkhieradmin

import android.Manifest
import android.app.Application
import android.app.NotificationManager
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import com.devYoussef.nb3elkhieradmin.constant.Constants
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App :Application() {
    override fun onCreate() {
        super.onCreate()

        val notificationManager = NotificationManagerCompat.from(this)
        val notificationChannel = NotificationChannelCompat.Builder(
            Constants.channelId,
            NotificationManager.IMPORTANCE_HIGH
        )
            .setName(Constants.channelName)
            .build()
        notificationManager.createNotificationChannel(notificationChannel)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }
}
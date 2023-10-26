package com.devYoussef.nb3elkhieradmin.utils

import android.Manifest
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devYoussef.nb3elkhieradmin.constant.Constants
import com.devYoussef.nb3elkhieradmin.ui.MainActivity
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationCompat.Builder
import com.devYoussef.nb3elkhieradmin.R
import com.google.firebase.messaging.FirebaseMessagingService


 class FirebaseMessagingService :FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e( "onMessageReceived: ",message.toString() )

        if (message.notification != null){
            val title = message.notification!!.title
            val body = message.notification!!.body
            generateNotification(title!!,body!!)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e( "onMessageReceived: ",token.toString() )
    }

    private fun generateNotification(title:String, message :String){

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)



        val pendingIntent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val builder : NotificationCompat.Builder = Builder(this , Constants.channelId)
            .setSmallIcon(R.drawable.logo1)
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(this)
        val notificationChannel = NotificationChannelCompat.Builder(Constants.channelId,
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(0,builder.build())

    }
}
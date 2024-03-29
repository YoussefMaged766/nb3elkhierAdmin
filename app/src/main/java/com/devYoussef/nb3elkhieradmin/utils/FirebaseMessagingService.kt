package com.devYoussef.nb3elkhieradmin.utils

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.devYoussef.nb3elkhieradmin.constant.Constants
import com.devYoussef.nb3elkhieradmin.ui.MainActivity
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationCompat.Builder
import androidx.navigation.NavDeepLinkBuilder
import com.devYoussef.nb3elkhieradmin.R
import com.google.firebase.messaging.FirebaseMessagingService


class FirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)


        if (message.notification != null) {
            Log.e("onMessageReceived: ", message.data["id"].toString())
            val title = message.notification!!.title
            val body = message.notification!!.body
            generateNotification(title!!, body!! , message.data["id"].toString())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("onMessageReceived: ", token.toString())
    }

    private fun generateNotification(title: String, message: String , data:String) {

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("id", data)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

//        val pendingIntent = PendingIntent.getActivity(applicationContext,1,intent,
//            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        Log.e( "generateNotification: ",intent.extras?.getString("id").toString() )

        val args = Bundle()
        args.putString("id", data)
        args.putString("type", "current")

        val pendingIntent = NavDeepLinkBuilder(this)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.detailsOrderFragment)
            .setArguments(args)
            .createPendingIntent()

//
//        pendingIntent.send()

        val builder: NotificationCompat.Builder = Builder(applicationContext, Constants.channelId)
            .setSmallIcon(R.drawable.logo2)
            .setContentTitle(title)
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val notificationManager = NotificationManagerCompat.from(this)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(0, builder.build())

    }
}
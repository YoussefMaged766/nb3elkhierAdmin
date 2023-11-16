package com.devYoussef.nb3elkhieradmin.constant

import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

object Constants {
    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    const val BASEURL = "https://nb3elker-78938e731963.herokuapp.com/"
//    const val BASEURL = "https://nb-lkhyr-p8d8.onrender.com"
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore("save")
    const val channelId = "notification_channel"
    const val channelName = "notification_channel_name"
    const val TOKEN = "token"

    suspend fun FirebaseMessaging.getTokenSuspended(): String? {
        return try {
            this.token.await()
        } catch (e: Exception) {
            null
        }
    }
}
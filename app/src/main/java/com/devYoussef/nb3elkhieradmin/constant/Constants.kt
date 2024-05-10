package com.devYoussef.nb3elkhieradmin.constant

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.util.Locale

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

    fun Fragment.getAddressFromLatLng(latitude :Double ,longitude:Double ): String {
        val geocoder = Geocoder(requireContext(), Locale("ar"))
        try {
            val addresses = geocoder.getFromLocation(latitude,longitude, 1)
            if (addresses?.isNotEmpty()!!) {
                val address = addresses[0]
                // You can format the address as per your requirements
                return address.getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            requireContext().showToast("حدث خطأ ما")
        }
        return "لا يوجد عنوان"
    }
}
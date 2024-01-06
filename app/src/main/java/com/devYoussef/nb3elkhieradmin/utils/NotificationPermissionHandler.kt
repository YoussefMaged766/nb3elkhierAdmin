package com.devYoussef.nb3elkhieradmin.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class NotificationPermissionHandler(
    private val fragment: Fragment,
    private val onPermissionResult: (Boolean) -> Unit
) {

    private val permissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            onPermissionResult(isGranted)
        }

    fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
            // Check if the permission is already granted
            val isPermissionGranted =
                ContextCompat.checkSelfPermission(fragment.requireContext(), notificationPermission) ==
                        PackageManager.PERMISSION_GRANTED

            if (isPermissionGranted) {
                // The permission is already granted
                onPermissionResult(true)
            } else {
                // Request the permission
                permissionLauncher.launch(notificationPermission)
            }
        } else {
            // Handle devices with SDK version below TIRAMISU if needed
            // You might want to handle it differently based on your requirements
            onPermissionResult(false)
        }
    }
}
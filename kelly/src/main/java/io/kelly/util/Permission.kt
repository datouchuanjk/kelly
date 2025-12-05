package io.kelly.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat

val Map<String, @JvmSuppressWildcards Boolean>.isAllGranted get() = values.all { it }

fun isPermissionGranted(permission: String): Boolean {
    return try {
        ActivityCompat.checkSelfPermission(
            ContextManager.app,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    } catch (_: Exception) {
        false
    }
}

fun isPermissionDenied(permission: String): Boolean {
    return !isPermissionGranted(permission)
}


fun isPermissionDeniedPermanently(permission: String): Boolean {
    val activity = ContextManager.resumedActivity ?: return false
    if (isPermissionGranted(permission)) return false
    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    return !showRationale
}

internal fun areNotificationsEnabled(channelName: String? = null): Boolean {
    val notificationManager = NotificationManagerCompat.from(ContextManager.app)
    val enabled = notificationManager.areNotificationsEnabled()
    if (!enabled) return false
    if (channelName.isNullOrEmpty()) {
        return true
    }
    val channel = notificationManager.getNotificationChannel(channelName)
    return if (channel == null) {
        false
    } else {
        channel.importance > NotificationManagerCompat.IMPORTANCE_NONE
    }
}

internal fun canRequestPackageInstalls(): Boolean {
    return ContextManager.app.packageManager.canRequestPackageInstalls()
}

internal fun isExternalStorageManager(useScopedStorageOnAndroid11: Boolean = true): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        if (useScopedStorageOnAndroid11) {
            true
        } else {
            Environment.isExternalStorageManager()
        }
    } else {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        permissions.all { isPermissionGranted(it) }
    }
}
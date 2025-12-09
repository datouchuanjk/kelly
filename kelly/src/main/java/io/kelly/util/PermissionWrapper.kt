package io.kelly.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

fun String.asPermissionWrapper() = PermissionWrapper(this)

@JvmInline
value class PermissionWrapper internal constructor(val name: String) {
    companion object

    val isGranted
        get() = ContextCompat.checkSelfPermission(
            ContextManager.context,
            name
        ) == PackageManager.PERMISSION_GRANTED

    val isDenied get() = !isGranted

    val isDeniedPermanently: Boolean
        get() {
            val activity = ContextManager.resumedActivity ?: return false
            if (isGranted) return false
            val showRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity, name)
            return !showRationale
        }
}

fun PermissionWrapper.Companion.areNotificationsEnabled(channelId: String? = null): Boolean {
    val notificationManager = NotificationManagerCompat.from(ContextManager.context)
    val enabled = notificationManager.areNotificationsEnabled()
    if (!enabled) return false
    if (channelId.isNullOrEmpty() || Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
        return true
    }
    val channel = notificationManager.getNotificationChannel(channelId)
    return if (channel == null) {
        false
    } else {
        channel.importance != NotificationManagerCompat.IMPORTANCE_NONE
    }
}

fun PermissionWrapper.Companion.canRequestPackageInstalls(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        ContextManager.context.packageManager.canRequestPackageInstalls()
    } else {
        true
    }
}

fun PermissionWrapper.Companion.isExternalStorageManager(useScopedStorageOnAndroid11: Boolean = true): Boolean {
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
        permissions.all { it.asPermissionWrapper().isGranted }
    }
}
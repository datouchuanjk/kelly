package io.kelly.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun buildNotification(
    channelId: String,
    title: String,
    content: String,
    @DrawableRes smallIcon: Int = ContextManager.app.applicationInfo.icon,
    priority: Int = NotificationCompat.PRIORITY_DEFAULT,
    autoCancel: Boolean = true,
    buildAction: NotificationCompat.Builder.() -> Unit = {}
): Notification {
    return NotificationCompat.Builder(ContextManager.app, channelId)
        .setContentTitle(title)
        .setContentText(content)
        .setSmallIcon(smallIcon)
        .setPriority(priority)
        .setAutoCancel(autoCancel)
        .apply(buildAction)
        .build()
}

@RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
fun Notification.notify(id: Int) {
    val manager = NotificationManagerCompat.from(ContextManager.app)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        areNotificationsEnabled(channelId)
    } else {
        areNotificationsEnabled()
    }.let {
        if (!it) {
            return
        }
    }
    manager.notify(id, this)
}

fun cancelNotification(id: Int) {
    NotificationManagerCompat.from(ContextManager.app).cancel(id)
}

fun buildNotificationChannel(
    channelId: String,
    importance: Int = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    channelName: String = channelId,
    description: String? = null,
    buildAction: NotificationChannelCompat.Builder.() -> Unit = {}
) {
    val manager = NotificationManagerCompat.from(ContextManager.app)
    val channelBuilder = NotificationChannelCompat.Builder(channelId, importance)
        .setName(channelName)
    if (description != null) {
        channelBuilder.setDescription(description)
    }
    manager.createNotificationChannel(
        channelBuilder
            .apply(buildAction)
            .build()
    )
}

fun deleteNotificationChannel(channelId: String) {
    val manager = NotificationManagerCompat.from(ContextManager.app)
    manager.deleteNotificationChannel(channelId)
}
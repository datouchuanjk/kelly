package io.kelly.util

import android.content.Intent
import android.net.Uri

fun jumpToGooglePlay(onFailed: () -> Unit) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=${ContextManager.app.packageName}")
        )
        intent.setPackage("com.android.vending")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextManager.app.startActivity(intent)
    } catch (_: Exception) {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${ContextManager.app.packageName}")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextManager.app.startActivity(intent)
        } catch (_: Exception) {
            onFailed()
        }
    }
}


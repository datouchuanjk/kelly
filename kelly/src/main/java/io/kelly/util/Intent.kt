package io.kelly.util

import android.content.Intent
import androidx.core.net.toUri

fun jumpToGooglePlay(onFailed: () -> Unit) {
    try {
        val intent = Intent(
            Intent.ACTION_VIEW,
            "market://details?id=${ContextManager.context.packageName}".toUri()
        )
        intent.setPackage("com.android.vending")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ContextManager.context.startActivity(intent)
    } catch (_: Exception) {
        try {
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://play.google.com/store/apps/details?id=${ContextManager.context.packageName}".toUri()
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ContextManager.context.startActivity(intent)
        } catch (_: Exception) {
            onFailed()
        }
    }
}


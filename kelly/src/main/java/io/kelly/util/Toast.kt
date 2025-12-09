package io.kelly.util

import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes


fun String?.toast(duration: Int = Toast.LENGTH_SHORT) = SingleToast.toast(this, duration)

fun @receiver:StringRes Int.toast(duration: Int = Toast.LENGTH_SHORT) = SingleToast.toast(this, duration)

internal object SingleToast {
    private val mainHandler = Handler(Looper.getMainLooper())
    @Volatile
    private var currentToast: Toast? = null

    fun toast(@StringRes resId: Int, duration: Int) {
        val text = try {
            ContextManager.context.getString(resId)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
            return
        }
        toast(text, duration)
    }

    fun toast(text: String?, duration: Int) {
        val validText = text?.trim()?.takeIf { it.isNotEmpty() } ?: return
        if (Looper.myLooper() == Looper.getMainLooper()) {
            showToast(validText, duration)
        } else {
            mainHandler.post {
                showToast(validText, duration)
            }
        }
    }

    private fun showToast(text: String, duration: Int) {
        try {
            currentToast?.cancel()
        } catch (_: Exception) {

        }

        try {
            currentToast = Toast.makeText(ContextManager.context, text, duration).apply {
                show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
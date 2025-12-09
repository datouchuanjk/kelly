package io.kelly.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.util.DisplayMetrics

val AppPackageName: String
    get() = ContextManager.context.packageName

val AppProcessName: String?
    get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }
        return ContextManager.context.getSystemService(ActivityManager::class.java)
            ?.runningAppProcesses
            ?.find { it.pid == Process.myPid() }
            ?.processName
    }


val AppVersionCode: Long
    get() = try {
        val pi = getPackageInfoCompat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pi.longVersionCode
        } else {
            pi.versionCode.toLong()
        }
    } catch (_: Exception) {
        0
    }

val AppVersionName: String
    get() = try {
        getPackageInfoCompat().versionName.orEmpty()
    } catch (_: Exception) {
        ""
    }


private fun getPackageInfoCompat() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextManager.context.packageManager.getPackageInfo(
            ContextManager.context.packageName,
            PackageManager.PackageInfoFlags.of(0)
        )
    } else {
        ContextManager.context.packageManager.getPackageInfo(ContextManager.context.packageName, 0)
    }

fun Context.createDensityContext(designWidthDp: Float): Context {
    val widthPixels = resources.displayMetrics.widthPixels
    val targetDensity = widthPixels / designWidthDp
    val targetDensityDpi = (targetDensity * DisplayMetrics.DENSITY_DEFAULT).toInt()
    val configuration = resources.configuration
    configuration.densityDpi = targetDensityDpi
    configuration.fontScale = 1f
    return createConfigurationContext(configuration)
}
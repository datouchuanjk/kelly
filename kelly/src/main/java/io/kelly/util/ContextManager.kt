package io.kelly.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle

fun Context.initKelly() {
    ContextManager.init(applicationContext as Application)
}

internal object ContextManager {
    lateinit var context: Application
        private set

    internal var resumedActivity by WeakDelegate<Activity>()
        private set

    val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("_sp", Context.MODE_PRIVATE)
    }

    fun init(application: Application) {
        context = application
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksImpl)
    }

    private object ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
        override fun onActivityResumed(activity: Activity) {
            if (resumedActivity != activity) {
                resumedActivity = activity
            }
        }

        override fun onActivityStopped(activity: Activity) {
            if (resumedActivity == activity) {
                resumedActivity = null
            }
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }
}
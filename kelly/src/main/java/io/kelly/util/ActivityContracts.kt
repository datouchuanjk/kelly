package io.kelly.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

    class RequestNotificationAccess(
        private val channelId: String? = null,
        private val forceRequestPermission: Boolean = false
    ) : ActivityResultContract<Unit, Boolean>() {
        companion object {
            private var KEY_REQUESTED_POST_NOTIFICATIONS by prefBool()
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            val shouldAskSystemPermission = if (forceRequestPermission) {
                true
            } else {
                !KEY_REQUESTED_POST_NOTIFICATIONS
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && shouldAskSystemPermission) {
                KEY_REQUESTED_POST_NOTIFICATIONS = true
                return ActivityResultContracts.RequestPermission()
                    .createIntent(context, Manifest.permission.POST_NOTIFICATIONS)
            }

            return Intent().apply {
                if (areNotificationsEnabled()) {
                    action = Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, ContextManager.app.packageName)
                    putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
                } else {
                    action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    putExtra(Settings.EXTRA_APP_PACKAGE, ContextManager.app.packageName)
                }
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            return SynchronousResult(true).takeIf { areNotificationsEnabled(channelId) }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return areNotificationsEnabled(channelId)
        }
    }

    class RequestStorageAccess(
        private val useScopedStorageOnAndroid11: Boolean = true
    ) : ActivityResultContract<Unit, Boolean>() {

        companion object {
            private val PERMISSIONS_LEGACY = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        override fun createIntent(context: Context, input: Unit): Intent {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
            } else {
                Intent(ActivityResultContracts.RequestMultiplePermissions.ACTION_REQUEST_PERMISSIONS).apply {
                    putExtra(
                        ActivityResultContracts.RequestMultiplePermissions.EXTRA_PERMISSIONS,
                        PERMISSIONS_LEGACY
                    )
                }
            }
        }

        override fun getSynchronousResult(
            context: Context,
            input: Unit
        ): SynchronousResult<Boolean>? {
            return SynchronousResult(true).takeIf {
                isExternalStorageManager(useScopedStorageOnAndroid11)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return isExternalStorageManager(useScopedStorageOnAndroid11)
        }
    }


    class UnifiedPickVisualMedia(
        private val maxItems: Int = 1,
    ) : ActivityResultContract<PickVisualMediaRequest, List<Uri>>() {

        private val multiplePicker by lazy {
            ActivityResultContracts.PickMultipleVisualMedia(maxItems)
        }

        private val singlePicker by lazy {
            ActivityResultContracts.PickVisualMedia()
        }

        override fun createIntent(context: Context, input: PickVisualMediaRequest): Intent {
            return if (maxItems > 1) {
                multiplePicker.createIntent(context, input)
            } else {
                singlePicker.createIntent(context, input)
            }
        }

        override fun parseResult(resultCode: Int, intent: Intent?): List<Uri> {
            return if (maxItems > 1) {
                multiplePicker.parseResult(resultCode, intent)
            } else {
                singlePicker.parseResult(resultCode, intent)
                    ?.let { listOf(it) }
                    ?: emptyList()
            }
        }
    }

    class CaptureLimitedVideo(
        private val durationLimit: Int = 15,
        private val videoQuality: Int = 1,
    ) : ActivityResultContracts.CaptureVideo() {

        override fun createIntent(context: Context, input: Uri): Intent {
            return super.createIntent(context, input)
                .putExtra(MediaStore.EXTRA_DURATION_LIMIT, durationLimit)
                .putExtra(MediaStore.EXTRA_VIDEO_QUALITY, videoQuality)
        }

}
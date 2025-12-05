package io.kelly.util

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.app.ActivityOptionsCompat

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageOnly(
    options: ActivityOptionsCompat? = null
) {
    launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
        options
    )
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchVideoOnly(
    options: ActivityOptionsCompat? = null,
) {
    launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly),
        options
    )
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageAndVideo(
    options: ActivityOptionsCompat? = null,
) {
    launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo),
        options
    )
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchMimeType(
    mime: String,
    options: ActivityOptionsCompat? = null,
) {
    launch(
        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mime)),
        options
    )
}




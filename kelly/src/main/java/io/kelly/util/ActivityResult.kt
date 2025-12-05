package io.kelly.util

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageAndVideo
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.SingleMimeType
import androidx.core.app.ActivityOptionsCompat

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageOnly(
    options: ActivityOptionsCompat? = null
) {
    launch(PickVisualMediaRequest(ImageOnly), options)
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchVideoOnly(
    options: ActivityOptionsCompat? = null,
) {
    launch(PickVisualMediaRequest(VideoOnly), options)
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchImageAndVideo(
    options: ActivityOptionsCompat? = null,
) {
    launch(PickVisualMediaRequest(ImageAndVideo), options)
}

fun ActivityResultLauncher<PickVisualMediaRequest>.launchMimeType(
    mime: String,
    options: ActivityOptionsCompat? = null,
) {
    launch(PickVisualMediaRequest(SingleMimeType(mime)), options)
}




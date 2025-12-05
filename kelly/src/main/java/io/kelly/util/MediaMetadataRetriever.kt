package io.kelly.util

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever

val String.firstFrame: Bitmap?
    get() {
        var retriever: MediaMetadataRetriever? = null
        return try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(this@firstFrame)
            retriever.getFrameAtTime(0)
        } catch (_: Exception) {
            null
        } finally {
            retriever?.release()
        }
    }
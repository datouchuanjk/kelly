package io.kelly.util
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build

val String.firstFrame: Bitmap?
    get() {
        if (this.isBlank()) return null
        val retriever = MediaMetadataRetriever()
        try {
            if (this.startsWith("http://") || this.startsWith("https://")) {
                retriever.setDataSource(this, HashMap())
            } else {
                retriever.setDataSource(this)
            }
            return retriever.getFrameAtTime(0)
        } finally {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    retriever.close()
                } else {
                    retriever.release()
                }
            } catch (_: Exception) {
            }
        }
    }
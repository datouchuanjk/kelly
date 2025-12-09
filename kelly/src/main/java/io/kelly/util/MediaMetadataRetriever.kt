package io.kelly.util
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import androidx.annotation.WorkerThread

@WorkerThread
fun String.getFirstFrame(): Bitmap? {
    if (this.isBlank()) return null
    val retriever = MediaMetadataRetriever()
    return try {
        if (this.startsWith("http://") || this.startsWith("https://")) {
            retriever.setDataSource(this, HashMap())
        } else {
            retriever.setDataSource(this)
        }
        retriever.getFrameAtTime(0)
    } catch (_: Exception) {
        null
    } finally {
        try {
            retriever.release()
        } catch (_: Exception) {
        }
    }
}
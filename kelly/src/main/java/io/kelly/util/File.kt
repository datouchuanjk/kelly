package io.kelly.util

import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest
import java.util.UUID

val File.mimeType: String?
    get() = MimeTypeMap.getSingleton()
        .getMimeTypeFromExtension(extension.lowercase())

fun File.ensureExists(requireDirectory: Boolean): Boolean {
    if (exists()) {
        if (requireDirectory && isFile) return false
        if (!requireDirectory && isDirectory) return false
        return true
    }
    return if (isDirectory) {
        mkdirs()
    } else {
        parentFile?.mkdirs()
        try {
            createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

val File.totalSize: Long
    get() {
        if (!exists()) return 0
        if (isFile) return length()
        var size = 0L
        walk().onEnter { true }.forEach {
            if (it.isFile) size += it.length()
        }
        return size
    }

val File.md5: String
    get() {
        if (!exists() || !isFile) return ""
        val digest = MessageDigest.getInstance("MD5")
        val buffer = ByteArray(8192)
        FileInputStream(this).use { input ->
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                digest.update(buffer, 0, bytesRead)
            }
        }
        return digest.digest().joinToString("") { "%02x".format(it) }
    }

fun File.toProviderUri(
    authority: String = "${ContextManager.app.packageName}.fileProvider"
): Uri = FileProvider.getUriForFile(ContextManager.app, authority, this)


fun generateTempCacheFile(extension: String): File {
    val ext = extension.trimStart('.')
    return File(ContextManager.app.cacheDir, "${UUID.randomUUID()}.$ext")
}
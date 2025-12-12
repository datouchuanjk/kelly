package io.kelly.util

import android.webkit.MimeTypeMap
import java.util.Locale

val String.extension: String?
    get() {
        val url = this
        if (url.isBlank()) return null
        return MimeTypeMap.getFileExtensionFromUrl(url)
            .takeIf { it.isNotEmpty() }
            ?.lowercase(Locale.getDefault())
            ?: url.substringBefore('?').substringAfterLast('.', "").lowercase(Locale.getDefault()).takeIf { it.isNotEmpty() }
    }

val String.mimeType: String?
    get() {
        val ext = this.extension ?: return null
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext)
    }

fun String.filterChinese(): String {
    return filter { it.isChinese }
}

fun String.filterEnglish(): String {
    return filter { it.isEnglish }
}

fun String.filterDigit(): String {
    return filter { it.isDigit() }
}

internal val Char.isChinese: Boolean
    get() = this in '\u4e00'..'\u9fa5'

internal val Char.isEnglish: Boolean
    get() = this in 'a'..'z' || this in 'A'..'Z'
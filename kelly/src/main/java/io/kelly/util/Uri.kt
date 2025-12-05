package io.kelly.util

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.InputStream
import java.io.OutputStream

fun Uri.insert(block: ContentValues.() -> Unit): Uri? {
    val values = ContentValues().apply(block)
    return ContextManager.app.contentResolver.insert(this, values)
}

fun Uri.delete(where: String? = null, selectionArgs: Array<String>? = null): Int {
    return ContextManager.app.contentResolver.delete(this, where, selectionArgs)
}

fun Uri.update(where: String? = null, selectionArgs: Array<String>? = null, block: ContentValues.() -> Unit): Int {
    val values = ContentValues().apply(block)
    return ContextManager.app.contentResolver.update(this, values, where, selectionArgs)
}

fun Uri.query(
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null
): Cursor? {
    return ContextManager.app.contentResolver.query(
        this,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )
}

val Uri.inputStream: InputStream?
    get() = try {
        ContextManager.app.contentResolver.openInputStream(this)
    } catch (_: Exception) {
        null
    }

val Uri.outputStream: OutputStream?
    get() = try {
        ContextManager.app.contentResolver.openOutputStream(this)
    } catch (_: Exception) {
        null
    }

val Uri.dimensions: Pair<Int, Int>
    get() {
        query(
            projection = arrayOf(
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT
            )
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val wIndex = cursor.getColumnIndex(MediaStore.MediaColumns.WIDTH)
                val hIndex = cursor.getColumnIndex(MediaStore.MediaColumns.HEIGHT)
                if (wIndex != -1 && hIndex != -1) {
                    return cursor.getInt(wIndex) to cursor.getInt(hIndex)
                }
            }
        }
        return 0 to 0
    }


val Uri.fileSize: Long
    get() {
        query(
            projection = arrayOf(OpenableColumns.SIZE)
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (sizeIndex != -1) {
                    return cursor.getLong(sizeIndex)
                }
            }
        }
        return 0L
    }
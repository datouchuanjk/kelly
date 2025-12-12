package io.kelly.util

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.WorkerThread
import java.io.InputStream
import java.io.OutputStream

@WorkerThread
fun Uri.insert(block: ContentValues.() -> Unit): Uri? {
    val values = ContentValues().apply(block)
    return ContextManager.context.contentResolver.insert(this, values)
}

@WorkerThread
fun Uri.delete(where: String? = null, selectionArgs: Array<String>? = null): Int {
    return ContextManager.context.contentResolver.delete(this, where, selectionArgs)
}

@WorkerThread
fun Uri.update(
    where: String? = null,
    selectionArgs: Array<String>? = null,
    block: ContentValues.() -> Unit
): Int {
    val values = ContentValues().apply(block)
    return ContextManager.context.contentResolver.update(this, values, where, selectionArgs)
}

@WorkerThread
fun Uri.query(
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null
): Cursor? {
    return ContextManager.context.contentResolver.query(
        this,
        projection,
        selection,
        selectionArgs,
        sortOrder
    )
}

val Uri.inputStream: InputStream?
    get() = try {
        ContextManager.context.contentResolver.openInputStream(this)
    } catch (_: Exception) {
        null
    }

val Uri.outputStream: OutputStream?
    get() = try {
        ContextManager.context.contentResolver.openOutputStream(this)
    } catch (_: Exception) {
        null
    }

@WorkerThread
fun Uri.getAspectRatio(): Float {
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
                val width =cursor.getInt(wIndex)
                val height  =cursor.getInt(hIndex)
                return  width.toFloat()/height
            }
        }
    }
    return 1f
}

@WorkerThread
fun Uri.getFileSize(): Long {
    var size = 0L
    ContextManager.context.contentResolver.query(
        this,
        arrayOf(OpenableColumns.SIZE),
        null, null, null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val index = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (index != -1) {
                size = cursor.getLong(index)
            }
        }
    }
    return size
}
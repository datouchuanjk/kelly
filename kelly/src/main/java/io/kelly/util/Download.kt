package io.kelly.util

import android.net.Uri
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


fun String.downloadToFile(outputFile: File) {
    var connection: HttpURLConnection? = null
    try {
        val url = URL(this)
        connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 15000
        connection.readTimeout = 30000
        connection.instanceFollowRedirects = true
        connection.requestMethod = "GET"
        connection.connect()
        if (connection.responseCode != HttpURLConnection.HTTP_OK) {
            throw IOException("Server returned HTTP ${connection.responseCode}: ${connection.responseMessage}")
        }
        connection.inputStream.use { input ->
            outputFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
    } catch (e: Exception) {
        if (outputFile.exists()) {
            outputFile.delete()
        }
        throw e
    } finally {
        connection?.disconnect()
    }
}


fun Uri.downloadToFile(outputFile: File) {
    val input = inputStream ?: throw FileNotFoundException("Cannot open stream for Uri: $this")
    try {
        input.use { inputStream ->
            outputFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
    } catch (e: Exception) {
        if (outputFile.exists()) {
            outputFile.delete()
        }
        throw e
    }
}


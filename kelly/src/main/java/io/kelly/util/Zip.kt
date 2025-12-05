package io.kelly.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipInputStream

fun File.unzip(destDir: File) {
    if (!exists()) return

    if (!destDir.exists()) {
        destDir.mkdirs()
    }

    val destCanonicalPath = destDir.canonicalPath

    ZipInputStream(FileInputStream(this)).use { zis ->
        var entry = zis.nextEntry
        while (entry != null) {
            val outputFile = File(destDir, entry.name)

            if (!outputFile.canonicalPath.startsWith(destCanonicalPath)) {
                throw SecurityException()
            }

            if (entry.isDirectory) {
                outputFile.mkdirs()
            } else {
                outputFile.parentFile?.mkdirs()
                FileOutputStream(outputFile).use { fos ->
                    zis.copyTo(fos)
                }
            }
            zis.closeEntry()
            entry = zis.nextEntry
        }
    }
}
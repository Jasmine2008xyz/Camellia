package com.luoyu.camellia.utils

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class ZipUtil {
    companion object {
        const val BUFFER = 1024
        
        fun unZip(filePath: String, zipDir: String): String {
        var name = ""
        try {
            ZipFile(filePath).use { zipFile ->
            val entries = zipFile.entries()

            while (entries.hasMoreElements()) {
                val entry = entries.nextElement() as ZipEntry
                if (entry.isDirectory) {
                    name = entry.name.removeSuffix("/")
                    val dir = File(zipDir, name)
                    if (!dir.exists()) {
                        dir.mkdirs()
                    }
                }
            }

            zipFile.entries().asSequence().forEach { entry ->
                if (!entry.isDirectory) {
                    BufferedInputStream(zipFile.getInputStream(entry)).use { inputStream ->
                        val file = File(zipDir, entry.name)
                        FileOutputStream(file).use { fos ->
                            BufferedOutputStream(fos, BUFFER).use { outputStream ->
                                inputStream.copyTo(outputStream, BUFFER)
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return name
        }
    }
}

package com.luoyu.camellia.utils

import android.content.Context

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile



class AssetsUtil {
    companion object {
        
    fun readAssetFile(context: Context, fileName: String): String {
        val stringBuilder = StringBuilder()
        try {
            context.assets.open(fileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line).append("\n")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
  }
}

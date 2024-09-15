package com.luoyu.utils

import android.app.Activity
import android.widget.Toast
import com.luoyu.camellia.BuildConfig
import com.luoyu.http.HttpSender


class Update(private val context: Activity) {

    init {
        checkForUpdate()
    }

    private fun checkForUpdate() {
        Toast.makeText(context, "正在检测更新...(确保挂好梯子)", Toast.LENGTH_SHORT).show()

        val versionCodeStr = HttpSender.get("https://jasmine2008xyz.github.io/Jasemine2008xyz.github.io/version")

        if (versionCodeStr.isEmpty()) {
            Toast.makeText(context, "创建连接失败，检查您的网络状况", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, "检测到更新版本，正在下载APK...", Toast.LENGTH_SHORT).show()

        val versionCode = versionCodeStr.toIntOrNull() ?: run {
            Toast.makeText(context, "版本号解析失败", Toast.LENGTH_SHORT).show()
            return
        }

        if (versionCode > BuildConfig.VERSION_CODE) {
            val downloadUrl = "https://jasmine2008xyz.github.io/Jasemine2008xyz.github.io/release/$versionCode"
            val filePath = if (context.packageName == "com.tencent.mobileqq") {
                PathUtil.getApkDataPath() + "update/Camellia.APK"
            } else {
                "/storage/emulated/0/Android/data/com.luoyu.camellia/update/Camellia.APK"
            }

            val downloadFunction = if (context.packageName == "com.tencent.mobileqq") {
                HttpSender::downloadToFileFromQQ
            } else {
                HttpSender::downloadToFileFromModule
            }

            downloadFunction(downloadUrl, filePath)
            

            Toast.makeText(
                context,
                "请求安装失败！请前往$filePath 进行手动安装",
                Toast.LENGTH_LONG
            ).show()

        } else {
            Toast.makeText(context, "当前已是最新正式版本！", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun create(context: Activity): Update {
            return Update(context)
        }
    }
}

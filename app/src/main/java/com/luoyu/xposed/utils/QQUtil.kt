package com.luoyu.xposed.utils

import com.luoyu.utils.ClassUtil
import de.robv.android.xposed.XposedHelpers

class QQUtil {
    companion object {
        @JvmStatic
        fun getCurrentUin(): String {
            return try {
                val appRuntime = getAppRuntime() ?: return ""
                XposedHelpers.callMethod(appRuntime, "getCurrentAccountUin") as? String ?: ""
            } catch (e: Exception) {
                ""
            }
        }
        
        @JvmStatic
        fun getCurrentNick(): String {
            return try {
                val appRuntime = getAppRuntime() ?: return ""
                XposedHelpers.callMethod(appRuntime, "getCurrentNickname") as? String ?: ""
            } catch (e: Exception) {
                ""
            }
        }

        @Throws(Exception::class)
        @JvmStatic
        fun getAppRuntime(): Any? {
            val sApplication = XposedHelpers.callStaticMethod(
                ClassUtil.get("com.tencent.common.app.BaseApplicationImpl"),
                "getApplication"
            )
            return XposedHelpers.callMethod(sApplication, "getRuntime")
        }
    }
}

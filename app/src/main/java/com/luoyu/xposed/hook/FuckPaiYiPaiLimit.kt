package com.luoyu.xposed.hook

import com.luoyu.utils.ClassUtil
import com.luoyu.utils.MethodUtil
import com.luoyu.xposed.base.HookEnv
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

@Xposed_Item_Controller(itemTag = "屏蔽拍一拍Timing", isApi = false)
class FuckPaiYiPaiLimit {

    @Xposed_Item_Entry
    fun work() {
        XposedBridge.hookMethod(
           /* MethodUtil.create(ClassUtil.get("com.tencent.mobileqq.paiyipai.PaiYiPaiHandler"))
                .setParamsLength(1)
                .setParams(String::class.java)
                .get(HookEnv.getHostClassLoader())*/
                XposedHelpers.findMethodsByExactParameters(ClassUtil.get("com.tencent.mobileqq.paiyipai.PaiYiPaiHandler"),Boolean::class.java,String::class.java)[0],
            object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    param.result = true
                }
            }
        )
    }
}

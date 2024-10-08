package com.luoyu.xposed.hook.api

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder
import com.luoyu.xposed.core.HookInstaller
import com.luoyu.dexfinder.IDexFinder

import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Method

@Xposed_Item_Controller(itemTag = "QQNTOnMessage(QQNT消息监听)", isApi = true)
class QQNTOnMessage {
    @Xposed_Item_Finder
    fun findMethod(finder: IDexFinder) {
        finder.findMethodsByPathAndUseString(arrayOf("QQNTOnMessage_1"),arrayOf("com.tencent.qqnt.msg"),arrayOf("[不支持的元素类型]", "[图片]", "[文件]", "[emoji]"))
    }
    @Xposed_Item_Entry
    fun entry() {
        val m: Method = HookInstaller.Method_Map.get("QQNTOnMessage_1")
        val method = XposedHelpers.findMethodsByExactParameters(m.declaringClass, m.returnType, m.parameterTypes)[0]
        XposedBridge.hookMethod(method, object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                
            }
        })
    }
}
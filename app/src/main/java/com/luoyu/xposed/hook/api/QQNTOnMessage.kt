package com.luoyu.xposed.hook.api

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder
import com.luoyu.xposed.core.HookInstaller
import com.luoyu.xposed.utils.QQUtil
import com.luoyu.xposed.hook.SelfResponseEmoji
import com.luoyu.dexfinder.IDexFinder
import com.luoyu.xposed.ModuleController

import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XC_MethodHook
import java.lang.reflect.Method


@Xposed_Item_Controller(itemTag = "QQNTOnMessage(QQNT消息监听)", isApi = true)
class QQNTOnMessage {

    // 通过IDexFinder找到目标方法
    @Xposed_Item_Finder
    fun findMethod(finder: IDexFinder) {
        finder.findMethodsByPathAndUseString(
            arrayOf("QQNTOnMessage_1"),
            arrayOf("com.tencent.qqnt.msg"),
            arrayOf("[不支持的元素类型]", "[图片]", "[文件]", "[emoji]")
        )
    }

    // 实现Xposed方法钩挂
    @Xposed_Item_Entry
    fun entry() {
        // 获取方法对象
        val m: Method? = HookInstaller.Method_Map["QQNTOnMessage_1"]

        // 判断方法是否为空
        if (m != null) {
            // 查找方法
            val method = XposedHelpers.findMethodsByExactParameters(m.declaringClass, ArrayList::class.java, ArrayList::class.java)[0]
            XposedBridge.hookMethod(method, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    // 检查开关是否开启
                    val isEnabled = ModuleController.Config.getBooleanData("自身回应/开关", false)
                    if (isEnabled) {
                            val list = param.args[0] as ArrayList<*>
                            val msgRecord = list.get(0)
                            val senderUin = XposedHelpers.getLongField(msgRecord, "senderUin")
                            if (senderUin.toString() == QQUtil.getCurrentUin()) {
                            SelfResponseEmoji().loadResponse(msgRecord)
                            }
                    }
                }
            })
        }
    }
}

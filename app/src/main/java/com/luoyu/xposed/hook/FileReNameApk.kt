package com.luoyu.xposed.hook

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick


import com.luoyu.xposed.base.QQApi
import com.luoyu.xposed.base.HookEnv
import com.luoyu.xposed.ModuleController
import com.luoyu.xposed.base.QRoute

import com.luoyu.utils.Classes
import com.luoyu.utils.ClassUtil
import com.luoyu.utils.MethodUtil
import com.luoyu.utils.Util
import com.luoyu.xposed.logging.LogCat

import com.luoyu.xposed.message.MsgUtil
import com.luoyu.xposed.message.MsgElementCreator
import com.luoyu.utils.Hooker

import com.tencent.qqnt.kernel.nativeinterface.FileElement
import com.tencent.qqnt.kernel.nativeinterface.TextElement

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

import java.lang.reflect.Method

import java.util.ArrayList
import java.util.HashMap

@Xposed_Item_Controller(itemTag = "文件重命名apk.1")
class FileReNameApk {

    @Xposed_Item_Entry
    fun start() {
       val clazz = ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.IKernelMsgService\$CppProxy")
        val contactClass = Classes.getContactClass()
        val iOperateCallbackClass = ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback")

        val method = XposedHelpers.findMethodBestMatch(
            clazz, 
            "sendMsg", 
            Long::class.java, 
            contactClass, 
            ArrayList::class.java, 
            HashMap::class.java, 
            iOperateCallbackClass
        )
        
        XposedBridge.hookMethod(method, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                val isEnabled = ModuleController.Config.getBooleanData("文件重命名apk.1/开关", false)
                if(!isEnabled) return
                val list: ArrayList<Any> = param.args[2] as ArrayList<Any>
                val fileElement: Any? = XposedHelpers.callMethod(list[0], "getFileElement")
                if (fileElement == null) return
                // 获取文件名
                val fileName_field = fileElement.javaClass.getDeclaredField("fileName")
                var fileName = fileName_field.get(fileElement) as String
           
                if(fileName.endsWith(".1")) {
                    fileName = fileName.replace("apk","APK")
                    fileName_field.set(fileElement, fileName.substringBeforeLast(".1"))
                }
        }     
    })
  }
}

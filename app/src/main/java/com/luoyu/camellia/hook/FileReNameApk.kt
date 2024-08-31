package com.luoyu.camellia.hook

import com.luoyu.camellia.annotations.Xposed_Item_Controller
import com.luoyu.camellia.annotations.Xposed_Item_Entry
import com.luoyu.camellia.annotations.Xposed_Item_UiClick
import com.luoyu.camellia.annotations.Xposed_Item_UiLongClick

import com.luoyu.camellia.base.MItem
import com.luoyu.camellia.base.QQApi

import com.luoyu.camellia.utils.Classes
import com.luoyu.camellia.utils.ClassUtil
import com.luoyu.camellia.utils.Util

import com.luoyu.camellia.qqmessage.MsgUtil

import com.tencent.qqnt.kernel.nativeinterface.FileElement

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
                val isEnabled = MItem.Config.getBooleanData("文件重命名apk.1/开关", false)
                if(!isEnabled) return
                val list: ArrayList<Any> = param.args[2] as ArrayList<Any>
                val fileElement: Any? = XposedHelpers.callMethod(list[0], "getFileElement")
                if (fileElement == null) return
                // 获取文件名
                val fileName_field = fileElement.javaClass.getDeclaredField("fileName")
                var fileName = fileName_field.get(fileElement) as String
             /*   if(fileName.endsWith("apk.1"/*, ignoreCase = true*/)) {
                    fileName.replace("apk.1","APK")
                    fileName_field.set(fileElement, fileName)
                } else if(fileName.endsWith("apk"/*, ignoreCase = true*/)) {
                    fileName.replace("apk","APK")
                    fileName_field.set(fileElement, fileName)
                }*/
                if(fileName.endsWith(".1")) {
                    fileName = fileName.replace("apk","APK")
                    fileName_field.set(fileElement, fileName.substringBeforeLast(".1"))
                }
                
        }     
    })
   /* XposedBridge.hookAllConstructors(ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.FileElement"), object: XC_MethodHook () {
                    override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    XposedHelpers.callMethod(param.thisObject, "setFileSize",9223372036854775807L)
                    XposedHelpers.callMethod(param.thisObject, "setFileMd5","666")
                    val file: FileElement = param.thisObject as FileElement
                    file.picWidth = 1024
                    file.picHeight = 1024
                    file.fileSha3=""
                    file.fileSha=""
                    file.file10MMd5=""
                    file.subElementType=9
                    file.transferStatus=1
                    file.fileUuid="/1122"
                    }
                })
                try{
    val m = FileElement::class.java.getDeclaredMethod("setFileSize")
    XposedBridge.hookMethod(m,object: XC_MethodHook () {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    param.args[0] = 9223372036854775807L
                    }})
    val m2 = FileElement::class.java.getDeclaredMethod("setFileMd5")
    XposedBridge.hookMethod(m2,object: XC_MethodHook () {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    param.args[0] = "666"
                    }})
    
    }catch(e: Exception) {
    }
    */
    
  }
  @Xposed_Item_UiLongClick
  fun click() {
  // 发不出去，差个参数
  try {
    val contact = QQApi.createContact(2,"240214519")
    val list: ArrayList<Any> = ArrayList()
    val element = ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.MsgElement").newInstance()
    val file: FileElement = FileElement()
    file.fileName="测试文件.zip"
    file.fileMd5="666"
    file.fileBizId=102
    file.subElementType=9
    file.transferStatus=4
    file.fileUuid="/1122"
    file.fileSize=9223372036854775807L
    XposedHelpers.setObjectField(element,"fileElement",file)
    list.add(element)
    MsgUtil.sendMsg(contact,list)
    }catch (e: Exception) {
        MItem.QQLog.e("错误",Util.getStackTraceString(e))
    }
        MItem.QQLog.d("测试","1")
  }
}

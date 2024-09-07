package com.luoyu.camellia.hook

import com.luoyu.camellia.annotations.Xposed_Item_Controller
import com.luoyu.camellia.annotations.Xposed_Item_Entry
import com.luoyu.camellia.annotations.Xposed_Item_UiClick
import com.luoyu.camellia.annotations.Xposed_Item_UiLongClick

import com.luoyu.camellia.base.MItem
import com.luoyu.camellia.base.QQApi
import com.luoyu.camellia.base.HookEnv

import com.luoyu.camellia.utils.Classes
import com.luoyu.camellia.utils.ClassUtil
import com.luoyu.camellia.utils.MethodUtil
import com.luoyu.camellia.utils.Util

import com.luoyu.camellia.qqmessage.MsgUtil
import com.luoyu.camellia.qqmessage.MsgElementCreator

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
                val isEnabled = MItem.Config.getBooleanData("文件重命名apk.1/开关", false)
                if(!isEnabled) return
               // MItem.QQLog.log("测试",param.args[4])
               // com.tencent.qqnt.kernel.api.impl.BaseService
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
                val file: FileElement = fileElement as FileElement
                file.transferStatus =1
        }     
    })
    
  /*  XposedBridge.hookAllConstructors(ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.FileElement"), object: XC_MethodHook () {
                    override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    val file: FileElement = param.thisObject as FileElement
                    if(file.fileName.endsWith(".1")) {
                        file.fileName = file.fileName.replace("apk","APK").substringBeforeLast(".1")
                    }
                    }
                })*/
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
  /*  val m = FileElement::class.java.getDeclaredMethod("setTransferStatus")
    XposedBridge.hookMethod(m,object: XC_MethodHook () {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    param.args[0] = 4
                    }})*/
                    XposedBridge.hookAllConstructors(ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.FileElement"), object: XC_MethodHook () {
                    override fun afterHookedMethod(param: MethodHookParam) {
                    super.afterHookedMethod(param)
                    val file: FileElement = param.thisObject as FileElement
                    file.transferStatus=1
                    }
                })
             //   val m = XposedHelpers.
                //MethodUtil.create(ClassUtil.get("com.tencent.qphone.base.util.QLog")).setMethodName("addLogItem").get(HookEnv.getSelfClassLoader())
                /*XposedHelpers.findAndHookMethod(ClassUtil.get("com.tencent.qphone.base.util.QLog"),"addLogItem",Byte::class.java,String::class.java,Int::class.java,String::class.java,Throwable::class.java,object: XC_MethodHook () {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    MItem.QQLog.d("测试","param_1: ${param.args[1]}, param_3: ${param.args[3]}, param_4: ${param.args[4]}")
                    }})*/
                 /*   val m = XposedHelpers.findMethodBestMatch(ClassUtil.get(/*"com.tencent.mobileqq.filemanager.nt.bi"*/"com.tencent.mobileqq.filemanager.nt.NTC2CFileTransferMgr"),"Z",ClassUtil.get("com.tencent.mobileqq.filemanager.data.FileManagerEntity"),Int::class.java,String::class.java)
                    //MethodUtil.create(ClassUtil.get("com.tencent.mobileqq.filemanager.nt.bi")).setMethodName("Z").get(HookEnv.getSelfClassLoader())
                    XposedBridge.hookMethod(m,object: XC_MethodHook () {
                    override fun beforeHookedMethod(param: MethodHookParam) {
                    super.beforeHookedMethod(param)
                    MItem.QQLog.d("a","0:${param.args[0]},1:${param.args[1]},2:${param.args[2]}")
                   // param.setResult(null)
                    }})
                    */
                    
  }
  @Xposed_Item_UiLongClick
  fun click() {
  // 发不出去
  try {
    val contact = QQApi.createContact(2,"240214519")
    val list: ArrayList<Any> = ArrayList()
    
  //  val element = ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.MsgElement").newInstance()
   val file: FileElement = FileElement()
    file.fileName="测试文件.apk"
    file.fileMd5="666"
    file.fileBizId=102
    file.subElementType=11
    file.transferStatus=1
    file.fileUuid="/1122"
    file.fileSize=9223372036854775807L
   // file.filePath="/sdcard/AndroidIDEProjects/Camellia/app/build/outputs/apk/debug/Camellia(0.0.1).APK"
    
   // XposedHelpers.setObjectField(element,"textElement",text)
   val msg =MsgElementCreator.createFileElement("/sdcard/Android/media/com.tencent.mobileqq/opatch/log/20240831.log")
//   XposedHelpers.setObjectField(msg,"textElement",null)
   XposedHelpers.setObjectField(msg,"fileElement",file)
  // list.add(MsgElementCreator.createTextElement("/sdcard/Android/media/com.tencent.mobileqq/opatch/log/20240831.log"))
    list.add(msg)
    MsgUtil.sendMsg(contact,list)
    }catch (e: Exception) {
        MItem.QQLog.e("错误",Util.getStackTraceString(e))
    }
        //MItem.QQLog.d("测试","1")
  }
  
}

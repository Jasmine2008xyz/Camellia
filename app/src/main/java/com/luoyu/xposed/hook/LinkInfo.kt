package com.luoyu.xposed.hook

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick

import com.luoyu.xposed.base.HookEnv
import com.luoyu.xposed.ModuleController

import com.luoyu.camellia.interfaces.IDexFinder
import com.luoyu.xposed.startup.HookInit

import com.luoyu.camellia.utils.ClassUtil
import com.luoyu.camellia.utils.MethodUtil

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

import java.lang.reflect.Method

import java.util.ArrayList

import net.bytebuddy.ByteBuddy
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.implementation.FixedValue
import net.bytebuddy.implementation.MethodCall
import net.bytebuddy.android.AndroidClassLoadingStrategy

@Xposed_Item_Controller(itemTag = "展示链接信息")
class LinkInfo {

    @Xposed_Item_Finder
    fun find(finder: IDexFinder) {
        finder.findMethodsByPathAndUseString(arrayOf("LinkInfo_1"),arrayOf("com.tencent.qqnt.msg"),arrayOf("[不支持的元素类型]", "[图片]", "[文件]", "[emoji]"))
    }
    
    @Xposed_Item_Entry
    fun start() {
  //  val m: Method = HookInit.Method_Map.get("LinkInfo_1") as Method
    // 借助查找到的方法做跳板反射获取所需方法
  //  val m2 = XposedHelpers.findMethodsByExactParameters(m.getDeclaringClass(),ArrayList::class.java,ArrayList::class.java)[0]
    /*MethodUtil.create(m.getDeclaringClass())
                            .setReturnType(ArrayList::class.java)
                            .setParams(ArrayList::class.java)
                            .get(HookEnv.getHostClassLoader())*/
   /* XposedBridge.hookMethod(m2,*/
  /* XposedBridge.hookAllConstructors(ClassUtil.get("com.tencent.mobileqq.aio.msg.AIOMsgItem"),object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
               // val list: ArrayList<*> = param.args[0] as ArrayList<*>
                val msgRecord = param.args[0]
                // list.get(0)
                val elements: ArrayList<*> = msgRecord.javaClass.getDeclaredField("elements").get(msgRecord) as ArrayList<*>
                for (msgElement in elements) {
                    val TextElement: Any? = XposedHelpers.callMethod(msgElement, "getTextElement")
                    if ( TextElement != null ) {
                    val linkInfo: Any? = XposedHelpers.callMethod(TextElement, "getLinkInfo")
                    if (linkInfo != null) XposedHelpers.callMethod(TextElement, "setLinkInfo", null)
                    }
                }
              }
            })
            */
            XposedBridge.hookAllConstructors(ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.LinkInfo"),object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                 param.thisObject = null
                }
            })
    }
}

/*package com.luoyu.xposed.hook
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.View
import android.content.Context

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder

import com.luoyu.xposed.data.table.HostInfo

import com.luoyu.dexfinder.IDexFinder
import com.luoyu.utils.ClassUtil
import com.luoyu.utils.MethodUtil
import com.luoyu.utils.Util

import com.luoyu.xposed.base.HookEnv
import com.luoyu.xposed.ModuleController

import com.luoyu.xposed.core.HookInstaller

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

import java.util.concurrent.Callable
import java.util.List

import java.lang.reflect.Method
import java.lang.reflect.Field

import net.bytebuddy.ByteBuddy
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.implementation.FixedValue
import net.bytebuddy.implementation.MethodCall
import net.bytebuddy.android.AndroidClassLoadingStrategy
@Xposed_Item_Controller(itemTag = "复制图片链接")
class CopyPicUrl {
  @Xposed_Item_Entry
  fun loadHook(){
    val AIOMSGITEM = ClassUtil.get("com.tencent.mobileqq.aio.msg.AIOMsgItem")
 /* val TEXTAIO = ClassUtil.get("com.tencent.mobileqq.aio.msglist.holder.component.text.AIOTextContentComponent")
    val BASE = ClassUtil.get("com.tencent.mobileqq.aio.msglist.holder.component.BaseContentComponent")
    val GETAIO = MethodUtil.create(BASE)
        .setReturnType(AIOMSGITEM)
        .get(HookEnv.getHostClassLoader())
    val HOOK = MethodUtil.create(TEXTAIO)
        .setReturnType(List::class.java)
        .get(HookEnv.getHostClassLoader())*/
    val method = MethodUtil.create(ClassUtil.get("com.tencent.qqnt.aio.menu.ui.QQCustomMenuExpandableLayout"))
        .setMethodName("setMenu")
        .get(HookEnv.getHostClassLoader())

    val respond = object : XC_MethodHook() {
        @Throws(Throwable::class)
        override fun beforeHookedMethod(param: MethodHookParam) {
            val isEnabled = ModuleController.Config.getBooleanData("长按消息菜单添加显示MsgRecord项/开关", false)
            if(!isEnabled) return
            val listf: Field = XposedHelpers.findFirstFieldByExactType(param.args[0]::class.java, List::class.java)
            val list: List<Any> = listf.get(param.args[0]) as List<Any>
                val aiof: Field = XposedHelpers.findFirstFieldByExactType(list[0]::class.java, AIOMSGITEM)
                val aio: Any = aiof.get(list[0])
                val msgRecord = MethodUtil.create(aio::class.java)
                                                .setMethodName("getMsgRecord")
                                                .get(loader = HookEnv.getHostClassLoader())
                                                .invoke(aio)
                // XposedHelpers.callMethod(aio, "getMsgRecord", aio)
                
                val callable = Callable<Any> {
                val context = HookEnv.getActivity()
                
                
                
                    
                
                    ""
                }
                
                val item = createAIOMenuItemQQNT(aio, "消息记录", com.luoyu.camellia.R.drawable.simple, callable)
                list.add(item)
            }
        }
        XposedBridge.hookMethod(method, respond)
  }
  @Throws(RuntimeException::class)
    fun createAIOMenuItemQQNT(aioMsg: Any, textName: String, id: Int, callable: Callable<Any>): Any? {
    val msgClass = ClassUtil.get("com.tencent.mobileqq.aio.msg.AIOMsgItem")
    val m: Method = HookInstaller.Method_Map["ShowMsgRecord_1"] as Method
    val declaringClass: Class<*> = m.declaringClass
    val menuItemClass: Class<*> = ByteBuddy()
        .subclass(declaringClass)
        // text (包括toString()，虽然没什么用)
        .method(ElementMatchers.returns(String::class.java))
        .intercept(FixedValue.value(textName))
        // id
        .method(ElementMatchers.named("b"))
        .intercept(FixedValue.value(id))
        // method
        .method(ElementMatchers.named("h"))
        .intercept(MethodCall.call(callable))
        // res id
        .method(ElementMatchers.named("c"))
        .intercept(FixedValue.value(id))
        .make()
        .load(
            declaringClass.classLoader,
            AndroidClassLoadingStrategy.Wrapping(
                HookEnv.getContext().getDir("generated", Context.MODE_PRIVATE)
            )
        )
        .getLoaded()

    return try {
        menuItemClass.getDeclaredConstructor(msgClass).newInstance(aioMsg)
        } catch (e: Exception) {
        throw RuntimeException(e.toString())
    }
}
}
*/
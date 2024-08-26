package com.luoyu.camellia.hook

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method

import java.util.ArrayList
import java.util.HashMap

import com.luoyu.camellia.annotations.Xposed_Item_Controller
import com.luoyu.camellia.annotations.Xposed_Item_Entry

import com.luoyu.camellia.base.MItem
import com.luoyu.camellia.utils.ClassUtil

/**
 * @author 小明
 * @date 2024/08/26
 * @describe 
 */
@Xposed_Item_Controller
class PicSummaryHook {
    private val TAG = "PicSummaryHook"

    @Xposed_Item_Entry
    fun hook() {
        // 如果图片外显开关关闭，则不执行hook
       // val isEnabled = MItem.Config.getBooleanData("图片外显/开关", false)
        //if(!isEnabled) return

        // 获取 sendMsg 方法
        val clazz = ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.IKernelMsgService\$CppProxy")
        val contactClass = ClassUtil.get("com.tencent.qqnt.kernelpublic.nativeinterface.Contact")
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
        // hook sendMsg 方法
        XposedBridge.hookMethod(method, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                val list: ArrayList<Any> = param.args[2] as ArrayList<Any> // 明确声明泛型类型并进行类型转换
                val picElement: Any? = XposedHelpers.callMethod(list[0], "getPicElement")
                if (picElement != null) {
                // 使用反射设置字段值
                    val field = picElement.javaClass.getDeclaredField("summary")
                    field.isAccessible = true // 如果字段是私有的，需要设置可访问性
                    field.set(picElement, "hello,kotlin.") // 注意: 这里需要确保字段类型与设置值匹配
                    }
            }
        })
    }
}
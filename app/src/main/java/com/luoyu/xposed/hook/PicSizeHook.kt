package com.luoyu.xposed.hook

import android.content.Context

import android.text.InputType

import android.view.Gravity

import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method

import java.util.ArrayList
import java.util.HashMap

import com.google.android.material.textview.MaterialTextView
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick

import com.luoyu.camellia.activities.helper.ActivityAttributes

import com.luoyu.xposed.base.HookEnv
import com.luoyu.xposed.ModuleController

import com.luoyu.utils.Classes
import com.luoyu.utils.ClassUtil
import com.luoyu.utils.Util

import com.luoyu.http.HttpSender

import com.google.android.material.dialog.MaterialAlertDialogBuilder

/**
 * @author 小明
 * @date 2024/08/29
 * @describe 
 */
@Xposed_Item_Controller(itemTag = "修改图片大小")
class PicSizeHook {
    private val TAG = "PicSizeHook"
    
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
                val isEnabled = ModuleController.Config.getBooleanData("修改图片大小/开关", false)
                if(!isEnabled) return
                
                val list: ArrayList<Any> = param.args[2] as ArrayList<Any>
                val picElement: Any? = XposedHelpers.callMethod(list[0], "getPicElement")
                if (picElement != null) {
                // 使用反射设置字段值
                    val field_h = picElement.javaClass.getDeclaredField("picHeight")
                    val field_w = picElement.javaClass.getDeclaredField("picWidth")
                    field_h.isAccessible = true
                    field_w.isAccessible = true
                    val setValueHeight = ModuleController.Config.getIntData("修改图片大小/height",-1)
                    val setValueWidth = ModuleController.Config.getIntData("修改图片大小/width",-1)
                    if(setValueHeight != -1) field_h.set(picElement,setValueHeight);
                    if(setValueWidth != -1) field_w.set(picElement,setValueWidth);
                }
            }
        })
    }
    
    @Xposed_Item_UiLongClick
    fun onLongClick(){
        val context: Context = ActivityAttributes.context
        val layout = LinearLayout(context)
        layout.setOrientation(LinearLayout.VERTICAL)
        layout.setGravity(Gravity.CENTER)
        
        val edit_view = EditText(context)
        edit_view.setHint("输入图片width，-1为不修改")
        val width_cache = ModuleController.Config.getIntData("修改图片大小/width",-1)
        if(width_cache == -1)
        edit_view.setText("")
        else edit_view.setText("$width_cache")
        edit_view.setGravity(Gravity.CENTER)
        edit_view.inputType = InputType.TYPE_CLASS_NUMBER
        
        val edit_view2 = EditText(context)
        edit_view2.setHint("输入图片height，-1为不修改")
        val height_cache = ModuleController.Config.getIntData("修改图片大小/height",-1)
        if(height_cache == -1) edit_view.setText("")
        else edit_view2.setText("$height_cache")
        edit_view2.setGravity(Gravity.CENTER)
        edit_view2.inputType = InputType.TYPE_CLASS_NUMBER
        
        layout.addView(edit_view)
        layout.addView(edit_view2)
        
        MaterialAlertDialogBuilder(context) 
            .setTitle("修改图片大小") 
            .setView(layout)
            .setPositiveButton("Leave") {
             _ , _ ->
              // Do nothing
               }
             .setNeutralButton("保存") { _ , _ ->
              // 这里不能toInt()因为Config要接受java.int但是toInt()会返回kotlin.Int
              // MItem.Config.putData("修改图片大小/width",edit_view.getText().toString().toInt())
              // MItem.Config.putData("修改图片大小/height",edit_view2.getText().toString().toInt())
              ModuleController.Config.putData("修改图片大小/width",Integer.parseInt(edit_view.getText().toString()))
              ModuleController.Config.putData("修改图片大小/height",Integer.parseInt(edit_view2.getText().toString()))
             }
            .show()
    }
}
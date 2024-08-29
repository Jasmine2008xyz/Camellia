package com.luoyu.camellia.hook

import android.content.Context

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

import com.luoyu.camellia.annotations.Xposed_Item_Controller
import com.luoyu.camellia.annotations.Xposed_Item_Entry
import com.luoyu.camellia.annotations.Xposed_Item_UiClick
import com.luoyu.camellia.annotations.Xposed_Item_UiLongClick

import com.luoyu.camellia.activities.helper.ActivityAttributes

import com.luoyu.camellia.base.HookEnv
import com.luoyu.camellia.base.MItem

import com.luoyu.camellia.utils.Classes
import com.luoyu.camellia.utils.ClassUtil
import com.luoyu.camellia.utils.Util

import com.luoyu.http.HttpSender

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.tencent.mobileqq.widget.QQToast


/**
 * @author 小明
 * @date 2024/08/26
 * @describe 
 */
@Xposed_Item_Controller(itemTag = "图片外显")
class PicSummaryHook {
    private val TAG = "PicSummaryHook"

    @Xposed_Item_Entry
    fun hook() {
        // 如果图片外显开关关闭，则不执行hook
      //  val isEnabled = MItem.Config.getBooleanData("图片外显/开关", false)
     //   if(!isEnabled) return

        // 获取 sendMsg 方法
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
        // hook sendMsg 方法
        XposedBridge.hookMethod(method, object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                val isEnabled = MItem.Config.getBooleanData("图片外显/开关", false)
                if(!isEnabled) return
                
                val list: ArrayList<Any> = param.args[2] as ArrayList<Any> // 明确声明泛型类型并进行类型转换
                val picElement: Any? = XposedHelpers.callMethod(list[0], "getPicElement")
                if (picElement != null) {
                // 使用反射设置字段值
                    val field = picElement.javaClass.getDeclaredField("summary")
                    field.isAccessible = true // 如果字段是私有的，需要设置可访问性
                    val setValue = MItem.Config.getStringData("图片外显/内容","No_Content")
                    if(MItem.Config.getBooleanData("图片外显/随机一言",false)) {
                            field.set(picElement,HttpSender.get("https://api.tangdouz.com/sjyy.php"))
                        }
                    else if(setValue!="No_Content") {
                            field.set(picElement, setValue)
                    }
                     // 注意: 这里需要确保字段类型与设置值匹配
                }
            }
        })
    }
    
    @Xposed_Item_UiClick
    @Deprecated(message = "设定上Switch不会触发onClick，所以弃用，暂时留着防止出什么bug")
    fun onClick() {
        QQToast.makeText(HookEnv.getActivity(),5,"Ui Clicked",0,0).show()
    }
    
    @Xposed_Item_UiLongClick
    fun onLongClick(){
        val context: Context = ActivityAttributes.context
        val layout = LinearLayout(context)
        layout.setOrientation(LinearLayout.VERTICAL)
        layout.setGravity(Gravity.CENTER)
        
        val branch1 = LinearLayout(context)
        branch1.setOrientation(LinearLayout.HORIZONTAL)
        branch1.setGravity(Gravity.CENTER)
        
        val edit_view = EditText(context)
        edit_view.setHint("输入图片外显内容")
        edit_view.setText(MItem.Config.getStringData("图片外显/内容",""))
        edit_view.setGravity(Gravity.CENTER)
        
        val btn = MaterialButton(context)
        btn.setOnClickListener {
            MItem.Config.putData("图片外显/内容",edit_view.getText().toString())
        }
        btn.setText("保存")
        btn.setGravity(Gravity.CENTER)
        
        branch1.addView(edit_view)
        branch1.addView(btn)
        
        val branch2 = LinearLayout(context)
        branch2.setOrientation(LinearLayout.HORIZONTAL)
        branch2.setGravity(Gravity.CENTER)
        
        val text_view = MaterialTextView(context)
        text_view.setText("随机一言")
        text_view.setTextColor(Util.parseColor("#1A1A1A"))
        text_view.setTextSize(16f)
        
        val switch = MaterialSwitch(context)
        switch.setChecked(MItem.Config.getBooleanData("图片外显/随机一言",false))
        switch.setOnCheckedChangeListener { _ , isChecked ->
            MItem.Config.putData("图片外显/随机一言",isChecked)
        }
        
        branch2.addView(text_view)
        branch2.addView(switch)
        
        layout.addView(branch1)
        layout.addView(branch2)
        
        MaterialAlertDialogBuilder(context) 
            .setTitle("图片外显") 
            .setView(layout)
            .setPositiveButton("Leave") {
             _ , _ ->
              // Do nothing
               }
            .show()
    }
}
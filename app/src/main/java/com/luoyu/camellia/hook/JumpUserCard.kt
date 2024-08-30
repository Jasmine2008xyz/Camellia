package com.luoyu.camellia.hook

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.luoyu.camellia.activities.helper.ActivityAttributes

import com.luoyu.camellia.annotations.Xposed_Item_Controller
import com.luoyu.camellia.annotations.Xposed_Item_UiClick

@Xposed_Item_Controller(itemTag = "跳转网页功能", isApi = false)
class JumpUserCard {
    @Xposed_Item_UiClick
    fun start() {
        val context = ActivityAttributes.context
        
        val items = arrayOf("打开QQ资料卡","打开群资料卡","查找共同群","自定义")
        val builder = MaterialAlertDialogBuilder(context)
                        .setTitle("跳转网页功能")
                        .setItems(items) { _, which ->
                            if(which == 0) {
                                
                            } else if(which == 1) {
                            
                            } else if(which == 2) {
                            
                            } else if(which == 3) {
                            
                            }
                        }
                        .setPositiveButton("Leave") { _, _ ->
                        
                        }
                        .show()
    }
}

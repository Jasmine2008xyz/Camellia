package com.luoyu.xposed.hook

import android.content.Context
import android.text.InputType

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.google.android.material.textfield.TextInputEditText

import com.luoyu.camellia.activities.helper.ActivityAttributes

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick

import com.luoyu.xposed.base.QQApi
import com.luoyu.xposed.ModuleController

@Xposed_Item_Controller(itemTag = "跳转网页功能", isApi = false)
class JumpUserCard {
    @Xposed_Item_UiClick
    fun start() {
        val context = ActivityAttributes.context
        
        val items = arrayOf("打开QQ资料卡","打开群资料卡","查找共同群","自定义")
        /*val builder = */MaterialAlertDialogBuilder(context)
                        .setTitle("跳转网页功能")
                        .setItems(items) { _ , which ->
                            if(which == 0) {
                                startOpenQQDialog(context)
                            } else if(which == 1) {
                                startOpenQQGroupDialog(context)
                            } else if(which == 2) {
                                startFindInCommonGroupDialog(context)
                            } else if(which == 3) {
                                startOpenUrlDialog(context)
                            }
                        }
                        .setPositiveButton("Leave") { _ , _ ->
                        
                        }
                        .show()
    }
    
    private fun startOpenQQDialog(con: Context) {
        val edit: TextInputEditText = TextInputEditText(con)
        edit.setHint("填入QQ号")
        edit.inputType = InputType.TYPE_CLASS_NUMBER
        MaterialAlertDialogBuilder(con)
                            .setTitle("打开QQ资料卡")
                            .setView(edit)
                            .setPositiveButton("Leave", null)
                            .setNeutralButton("确定") { _ , _ ->
                                QQApi.openQQ(con, edit.getText().toString())
                            }
                            .show()
    }
    
    private fun startOpenQQGroupDialog(con: Context) {
        val edit: TextInputEditText = TextInputEditText(con)
        edit.setHint("填入群号")
        edit.inputType = InputType.TYPE_CLASS_NUMBER
        MaterialAlertDialogBuilder(con)
                            .setTitle("打开群资料卡")
                            .setView(edit)
                            .setPositiveButton("Leave", null)
                            .setNeutralButton("确定") { _ , _ ->
                                QQApi.openGroup(con, edit.getText().toString())
                            }
                            .show()
    }
    
    private fun startFindInCommonGroupDialog(con: Context) {
        val edit: TextInputEditText = TextInputEditText(con)
        edit.setHint("填入QQ号")
        edit.inputType = InputType.TYPE_CLASS_NUMBER
        MaterialAlertDialogBuilder(con)
                            .setTitle("查找共同群")
                            .setView(edit)
                            .setPositiveButton("Leave", null)
                            .setNeutralButton("确定") { _ , _ ->
                                QQApi.findCommonGroup(con, edit.getText().toString())
                            }
                            .show()
    }
    
    private fun startOpenUrlDialog(con: Context) {
        val edit: TextInputEditText = TextInputEditText(con)
        edit.setHint("填入链接")
        MaterialAlertDialogBuilder(con)
                            .setTitle("自定义")
                            .setView(edit)
                            .setPositiveButton("Leave", null)
                            .setNeutralButton("确定") { _ , _ ->
                                QQApi.openUrl(con, edit.getText().toString())
                            }
                            .show()
    }
}

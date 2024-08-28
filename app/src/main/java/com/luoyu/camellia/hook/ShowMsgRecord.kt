package com.luoyu.camellia.hook

import com.luoyu.camellia.annotations.Xposed_Item_Controller
import com.luoyu.camellia.annotations.Xposed_Item_Entry
import com.luoyu.camellia.annotations.Xposed_Item_Finder
import com.luoyu.camellia.interfaces.IDexFinder
import com.luoyu.camellia.utils.ClassUtil

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers

@Xposed_Item_Controller(itemTag = "长按消息菜单添加显示MsgRecord项")
class ShowMsgRecord {
    
    @Xposed_Item_Finder
    fun find(finder: IDexFinder) {
        finder.findMethodsByPathAndUseString(
            arrayOf("ShowMsgRecord_1"),
            arrayOf("com.tencent.qqnt.aio.menu.ui"),
            arrayOf("QQCustomMenuItem{title=")
        )
    }

    @Xposed_Item_Entry
    fun start() {
        val AIOMSGITEM = ClassUtil.get("com.tencent.mobileqq.aio.msg.AIOMsgItem");
        val TEXTAIO =
            ClassUtil.get(
                "com.tencent.mobileqq.aio.msglist.holder.component.text.AIOTextContentComponent");
        val BASE =
            ClassUtil.get("com.tencent.mobileqq.aio.msglist.holder.component.BaseContentComponent");
       // val GETAIO = RMethodMatcher.Create(BASE).ReturnType(AIOMSGITEM).get();
       // val HOOK = RMethodMatcher.Create(TEXTAIO).ReturnType(List.class).get();
    }
}
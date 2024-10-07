package com.luoyu.xposed.hook.api

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder
import com.luoyu.dexfinder.IDexFinder
@Xposed_Item_Controller(itemTag = "QQNTOnMessage(QQNT消息监听)", isApi = true)
class QQNTOnMessage {
    @Xposed_Item_Finder
    fun findMethod(finder: IDexFinder) {
        finder.findMethodsByPathAndUseString(arrayOf("AutoOpenRedPacket_1"),arrayOf("com.tencent.qqnt.msg"),arrayOf("[不支持的元素类型]", "[图片]", "[文件]", "[emoji]"))
    }
}
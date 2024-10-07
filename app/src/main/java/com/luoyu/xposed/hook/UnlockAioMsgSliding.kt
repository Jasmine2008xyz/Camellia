package com.luoyu.xposed.hook;

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import com.luoyu.utils.ClassUtil
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Method

@Xposed_Item_Controller(itemTag = "解锁所有消息左滑")
class UnlockAioMsgSliding {

    @Xposed_Item_Entry
    fun start() {
        XposedHelpers.findAndHookMethod(ClassUtil.get("com.tencent.mobileqq.ark.api.impl.ArkHelperImpl"), "isSupportReply", String.class,
                    String.class, String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) {
                            param.setResult(true);
                        }
                    });
    }
    
}
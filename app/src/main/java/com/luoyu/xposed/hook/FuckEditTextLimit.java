package com.luoyu.xposed.hook;

import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

@Xposed_Item_Controller(itemTag = "解锁字数限制")
public class FuckEditTextLimit {
  @Xposed_Item_Entry
  public void start() {
    XposedHelpers.findAndHookMethod(
        "com.tencent.mobileqq.aio.input.sendmsg.AIOSendMsgVMDelegate",
        HookEnv.getHostClassLoader(),
        "v",
        java.util.List.class,
        new XC_MethodHook() {
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            param.setResult(false);
            super.beforeHookedMethod(param);
          }
        });

    XposedHelpers.findAndHookMethod(
        "com.tencent.mobileqq.aio.input.sendmsg.AIOSendMsgVMDelegate",
        HookEnv.getHostClassLoader(),
        "w",
        new XC_MethodHook() {
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            param.setResult(false);
            super.beforeHookedMethod(param);
          }
        });
  }
}

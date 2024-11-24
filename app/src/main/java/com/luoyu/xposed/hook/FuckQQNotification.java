package com.luoyu.xposed.hook;

import com.luoyu.utils.Reflex;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.logging.LogCat;
import java.lang.reflect.Method;

@Xposed_Item_Controller(itemTag = "屏蔽QQ通知")
public class FuckQQNotification {
  @Xposed_Item_Entry
  public void start() {
    Class<?> clazz = Reflex.loadClass("com.tencent.qqnt.notification.NotificationFacade");
    Method method =
        Reflex.findMethod(clazz)
            .setReturnType(void.class)
            .setParams(
                Reflex.loadClass("mqq.app.AppRuntime"),
                Object.class,
                Reflex.loadClass("com.tencent.qqnt.kernel.nativeinterface.NotificationCommonInfo"),
                Reflex.loadClass("com.tencent.qqnt.kernel.nativeinterface.RecentContactInfo"))
            .get();/*.setMethodName("G").get();*/
    XposedBridge.hookMethod_Before(method,param ->{
      param.setResult(null);
    });
  }
}

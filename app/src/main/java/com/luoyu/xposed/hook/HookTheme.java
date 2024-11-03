package com.luoyu.xposed.hook;

import android.os.Bundle;
import android.widget.FrameLayout;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Method;
import android.view.ViewTreeObserver.OnWindowAttachListener;

@Xposed_Item_Controller(itemTag = "劫持主题", isApi = true)
public class HookTheme {
  @Xposed_Item_Entry
  public void start() {
    Method method =
        XposedHelpers.findMethodBestMatch(
            ClassUtil.get("com.tencent.mobileqq.activity.QQBrowserActivity"),
            "onCreate",
            Bundle.class);
    XposedBridge.hookMethod_After(
        method,
        param -> {
          FrameLayout frame =
              (FrameLayout) XposedHelpers.getObjectField(param.thisObject, "fragmentContentView");
          frame
              .getViewTreeObserver()
              .addOnWindowAttachListener(
                  new OnWindowAttachListener() {
                    @Override
                    public void onWindowAttached() {
                      try {

                        Object webView =
                            XposedHelpers.findMethodBestMatch(
                                    param.thisObject.getClass(), "getHostWebView")
                                .invoke(param.thisObject);
                
                      } catch (Exception e) {

                      }
                    }

                    @Override
                    public void onWindowDetached() {}
                  });
        });
    Method method2 =
        XposedHelpers.findMethodsByExactParameters(
            ClassUtil.get("com.tencent.mobileqq.app.ThemeHandler"),
            Boolean.TYPE,
            ClassUtil.get("com.tencent.qphone.base.remote.ToServiceMsg"),
            ClassUtil.get("com.tencent.pb.theme.ThemeAuth$SubCmd0x1RspAuth"),
            String.class,
            String.class)[0];
    XposedBridge.hookMethod_Before(
        method2,
        param -> {
          param.setResult(null);
        });
  }
}

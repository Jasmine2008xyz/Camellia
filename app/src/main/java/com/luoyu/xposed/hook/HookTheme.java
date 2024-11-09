package com.luoyu.xposed.hook;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.logging.LogCat;
import com.luoyu.xposed.utils.JsBridge;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Method;
import android.view.ViewTreeObserver.OnWindowAttachListener;
import java.util.concurrent.atomic.AtomicReference;

@Xposed_Item_Controller(itemTag = "劫持主题", isApi = false)
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
                        Object webViewClient =
                            XposedHelpers.callMethod(webView, "getWebViewClient");
                        Method method3 =
                            XposedHelpers.findMethodBestMatch(
                                webViewClient.getClass(),
                                "onPageFinished",
                                ClassUtil.get("com.tencent.smtt.sdk.WebView"),
                                String.class);
                        AtomicReference atomicReference = new AtomicReference();
                        /*atomicReference.set(
                        de.robv.android.xposed.XposedBridge.hookMethod(
                            method3,
                            new XC_MethodHook() {
                              @Override
                              protected void beforeHookedMethod(MethodHookParam param) {
                                Unhook unHook = (Unhook)atomicReference.get(null);
                              }
                            }));*/
                        XposedBridge.hookMethod_Before_Once(
                            method3,
                            param -> {
                              Object arg0 = param.args[0];
                              String link = (String) param.args[1];
                              if (link.startsWith("https://zb.vip.qq.com/v2/pages/itemDetail")
                                  || link.startsWith("https://zb.vip.qq.com/mall/item-detail")) {
                                XposedHelpers.callMethod(
                                    arg0,
                                    "loadUrl",
                                    new Class[] {String.class},
                                    new Object[] {
                                      "javascript:\nfunction addView(vi){\n    var ready_state = document.readyState;\n    if(ready_state === 'interactive' || ready_state === 'complete') {\n    vi()\n  }else{\n      window.addEventListener(\"DOMContentLoaded\",vi);\n    }\n}"
                                    });
                                String id = JsBridge.a(link, "appid=");
                                if (id.equals("2") /*&& i.d().c("config", "bubbleSwitch")*/) {
                                  StringBuilder sb = new StringBuilder();
                                  sb.append(
                                      "javascript:addView(function () {    var dom = document.querySelector(");
                                  String str;
                                  if (link.startsWith("https://zb.vip.qq.com/mall/item-detail")) {
                                    str = "'.ui-main-panel'";
                                  } else {
                                    str = "'.goods-header-cont'";
                                  }

                                  sb.append(str);
                                  sb.append(
                                      ");\n    var btn=document.createElement(\"a\");\n    dom.appendChild(btn);   btn.innerHTML=\"<a href='javascript:window.Theme_utils.setBubble(window.getItemID())'><button  style='font-size:18px; color:#9900ff; margin: 40px 25px;'>设置气泡</button>\";\n});");

                                  XposedHelpers.callMethod(
                                      arg0,
                                      "loadUrl",
                                      new Class[] {String.class},
                                      new Object[] {sb.toString()});
                                }

                                if (id.equals("3")) {
                                  StringBuilder sb = new StringBuilder();
                                  sb.append(
                                      "javascript:addView(function () {    var dom = document.querySelector(");
                                  String str;
                                  if (link.startsWith("https://zb.vip.qq.com/mall/item-detail")) {
                                    str = "'.ui-main-panel'";
                                  } else {
                                    str = "G256PNLdM/xUJXw32MttslMuaXQ=\n";
                                  }

                                  sb.append(str);
                                  sb.append(
                                      ");\n    var btn=document.createElement(\"a\");\n    dom.appendChild(btn);   btn.innerHTML=\"<a href='javascript:window.Theme_utils.setTheme(window.getItemID())'><button  style='font-size:18px; color:#9900ff; margin: 40px 25px;'>设置主题</button>\";\n});");

                                  XposedHelpers.callMethod(
                                      arg0,
                                      "loadUrl",
                                      new Class[] {String.class},
                                      new Object[] {sb.toString()});
                                }
                                XposedHelpers.callMethod(
                                    arg0,
                                    "loadUrl",
                                    new Class[] {String.class},
                                    new Object[] {
                                      "javascript: function getItemID(){ var di=document.querySelector('.ui-item-basic-info'); \n return di.attributes['vt-busi'].value; }"
                                    });
                              }
                            });
                        XposedHelpers.callMethod(
                            webView,
                            "setWebViewClient",
                            new Class[] {ClassUtil.get("com.tencent.smtt.sdk.WebViewClient")},
                            new Object[] {webViewClient});
                        XposedHelpers.callMethod(
                            webView,
                            "addJavascriptInterface",
                            new Class[] {Object.class, String.class},
                            new Object[] {new JsBridge(), "Theme_utils"});
                      } catch (Exception e) {
                        LogCat.e("劫持主题/气泡", Log.getStackTraceString(e));
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

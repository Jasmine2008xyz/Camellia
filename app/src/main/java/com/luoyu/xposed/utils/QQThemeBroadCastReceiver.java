package com.luoyu.xposed.utils;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.temp.MMethod;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.QRoute;
import com.luoyu.xposed.logging.LogCat;
import com.luoyu.xposed.utils.QQUtil;
import de.robv.android.xposed.XposedHelpers;

public class QQThemeBroadCastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Class<String> clazz = String.class;
    if (intent != null
        && HookEnv.get("packageName").equals("com.tencent.mobileqq")
        && intent.getAction().equals("theme_set")) {
      int i = 2;
      if (i == 2 || i == 3) {
        int intExtra = intent.getIntExtra("type", 0);
        if (
        /*i.d().c("config", "themeSwitch") &&*/ intExtra == 1) {
          try {
            Toast.makeText(
                HookEnv.getContext(),
                ((Boolean)
                            MMethod.CallMethod(
                null,
                                ClassUtil.get("com.tencent.mobileqq.vas.theme.ThemeSwitcher"),
                                "startSwitch",
                                Boolean.TYPE,
                                new Class[] {
                                  String.class,
                                  String.class,
                                  ClassUtil.get(
                                      "com.tencent.mobileqq.vas.theme.api.IThemeSwitchCallback")
                                },
                                new Object[] {intent.getStringExtra("theme_id"), "211", null}))
                        .booleanValue()
                    ? "设置成功"
                    : "设置失败",
                0).show();
            return;
          } catch (Exception e) {
LogCat.e("QQTheme",Log.getStackTraceString(e));
          }
        } else if (
        /*!i.d().c("config", "bubbleSwitch") ||*/ intExtra != 2) {
          return;
        } else {
          try {
            XposedHelpers.callMethod(
                XposedHelpers.callMethod(
                    QQUtil.getAppRuntime(),
                    "getBusinessHandler",
                    new Class[] {String.class},
                    new Object[] {
                      (String)
                          XposedHelpers.callMethod(
                              QRoute.api(ClassUtil.get("com.tencent.mobileqq.vas.svip.api.ISVIPHandlerProxy")),
                              "getImplClassName",
                              new Object[0])
                    }),
                "setSelfBubbleId",
                new Class[] {Integer.TYPE},
                new Object[] {Integer.valueOf(intent.getStringExtra("bubble_id"))});
            Toast.makeText(HookEnv.getContext(),"设置成功",0).show();
            return;
          } catch (Exception e2) {
            LogCat.e("QQTheme",Log.getStackTraceString(e2));
          }
        }
       // androidx.activity.result.a.d(str, str2, sb, e);
      }
    }
  }
}

package com.luoyu.xposed.core;

import android.content.Context;
import android.content.IntentFilter;
import com.luoyu.camellia.activities.support.ActivityProxyManager;
import com.luoyu.utils.AppUtil;
import com.luoyu.utils.PathUtil;
import com.luoyu.utils.XRes;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.data.table.HostInfo;
import com.luoyu.xposed.utils.QQThemeBroadCastReceiver;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import com.luoyu.utils.ClassUtil;
import android.app.Activity;
import java.util.concurrent.atomic.AtomicBoolean;

public class BaseHook {
  private static HookInstaller hookInstaller;
  private static final AtomicBoolean IsLoad = new AtomicBoolean();

  public BaseHook() {
    XposedHelpers.findAndHookMethod(
        ClassUtil.load("com.tencent.mobileqq.qfix.QFixApplication"),
        "attachBaseContext",
        Context.class,
        new XC_MethodHook() {
          @Override
          protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
            Context context = (Context) param.args[0];
            HookEnv.put("HostContext", context);
            /*
             * 取代 {@link com.luoyu.xposed.startup.HookInit#constructor(XC_LoadPackage.LoadPackageParam)} 中获取到的ClassLoader
             * 防止有框架传递了不正确的类加载器
             */
            //    HookEnv.put("HostClassLoader", context.getClass().getClassLoader());
            XRes.addAssetsPath(HookEnv.getContext());
            ActivityProxyManager.initActivityProxyManager(
                HookEnv.getContext(), PathUtil.getApkPath(), com.luoyu.camellia.R.string.app_name);
            // 启动需要获取到Context后才能继续进行的代码
            if (!IsLoad.getAndSet(true)) {
              // Init BroadCastReceiver
              context.registerReceiver(
                  new QQThemeBroadCastReceiver(), new IntentFilter("theme_set"));
              // 获取QQ信息
              HostInfo.QQVersionName = AppUtil.getVersionNameInt(HookEnv.getContext()) + "";
              HostInfo.QQVersionCode = AppUtil.getVersionCode(HookEnv.getContext());
              // 加载自身Hook项目
              hookInstaller = new HookInstaller();
            }
          }
        });

    XposedHelpers.findAndHookMethod(
        Activity.class,
        "onResume",
        new XC_MethodHook() {
          @Override
          protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            Activity activity = (Activity) param.thisObject;
            XRes.addAssetsPath(activity);
            HookEnv.put("HostActivity", activity);
          }
        });

    XposedHelpers.findAndHookConstructor(
        ClassUtil.load("com.tencent.mobileqq.app.QQAppInterface"),
        ClassUtil.load("com.tencent.common.app.BaseApplicationImpl"),
        String.class,
        new XC_MethodHook() {
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            HookEnv.put("AppInterFace", param.thisObject);
          }
        });
  }
}

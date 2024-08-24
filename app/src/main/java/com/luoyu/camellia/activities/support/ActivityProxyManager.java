package com.luoyu.camellia.activities.support;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressLint({"DiscouragedPrivateApi", "PrivateApi"})
public class ActivityProxyManager {
  public static final String TAG = "ActivityProxyManager(活动代理管理器)";
  private static final AtomicBoolean Initialized = new AtomicBoolean();
  public static int ResId;
  public static String ModuleApkPath;
  public static Context HostContext;
  public static String HostActivityClassName;
  public static ClassLoader ModuleClassLoader;
  public static ClassLoader HostClassLoader;

  public static void initActivityProxyManager(
      Context hostContext, String ModuleApkPath, int Resid) {
    if (ResId != 0) ResId = Resid;

    HostClassLoader = hostContext.getClassLoader();
    ModuleClassLoader = ActivityProxyManager.class.getClassLoader();
    ModuleApkPath = ModuleApkPath;
    HostContext = hostContext;

    HostActivityClassName = getAllActivity(hostContext)[0].name;
    if (Initialized.getAndSet(true)) return;
    try {
      Class<?> cActivityThread = Class.forName("android.app.ActivityThread");
      // 获取sCurrentActivityThread对象
      Field fCurrentActivityThread = cActivityThread.getDeclaredField("sCurrentActivityThread");
      fCurrentActivityThread.setAccessible(true);
      Object currentActivityThread = fCurrentActivityThread.get(null);

      replaceInstrumentation(currentActivityThread);
      replaceHandler(currentActivityThread);
      replaceIActivityManager();
      try {
        replaceIActivityTaskManager();
      } catch (Exception ignored) {
        //
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static ActivityInfo[] getAllActivity(Context context) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packageInfo;
    try {
      packageInfo =
          packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
      // 所有的Activity
      ActivityInfo[] activities = packageInfo.activities;
      return activities;

    } catch (PackageManager.NameNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
    private static void replaceInstrumentation(Object activityThread) throws Exception {
        Field fInstrumentation = activityThread.getClass().getDeclaredField("mInstrumentation");
        fInstrumentation.setAccessible(true);
        Instrumentation mInstrumentation = (Instrumentation) fInstrumentation.get(activityThread);
        fInstrumentation.set(activityThread, new ProxyInstrumentation(mInstrumentation));
    }

    private static void replaceHandler(Object activityThread) throws Exception {
        Field fHandler = activityThread.getClass().getDeclaredField("mH");
        fHandler.setAccessible(true);
        Handler mHandler = (Handler) fHandler.get(activityThread);

        Class<?> chandler = Class.forName("android.os.Handler");
        Field fCallback = chandler.getDeclaredField("mCallback");
        fCallback.setAccessible(true);
        Handler.Callback mCallback = (Handler.Callback) fCallback.get(mHandler);
        fCallback.set(mHandler, new ProxyHandler(mCallback));
    }

    private static void replaceIActivityManager() throws Exception {
        Class<?> activityManagerClass;
        Field gDefaultField;
        try {
            activityManagerClass = Class.forName("android.app.ActivityManagerNative");
            gDefaultField = activityManagerClass.getDeclaredField("gDefault");
        } catch (Exception err1) {
            try {
                activityManagerClass = Class.forName("android.app.ActivityManager");
                gDefaultField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
            } catch (Exception err2) {
                return;
            }
        }
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null);
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        Object mInstance = mInstanceField.get(gDefault);
        Object amProxy = Proxy.newProxyInstance(
                ModuleClassLoader,
                new Class[]{Class.forName("android.app.IActivityManager")},
                new IActivityManagerHandler(mInstance));
        mInstanceField.set(gDefault, amProxy);
    }

    private static void replaceIActivityTaskManager() throws Exception {
        Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
        Field fIActivityTaskManagerSingleton = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
        fIActivityTaskManagerSingleton.setAccessible(true);
        Object singleton = fIActivityTaskManagerSingleton.get(null);
        Class<?> activityManagerClass;
        Field gDefaultField;
        try {
            activityManagerClass = Class.forName("android.app.ActivityManagerNative");
            gDefaultField = activityManagerClass.getDeclaredField("gDefault");
        } catch (Exception err1) {
            try {
                activityManagerClass = Class.forName("android.app.ActivityManager");
                gDefaultField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
            } catch (Exception err2) {
                return;
            }
        }
        gDefaultField.setAccessible(true);
        Object gDefault = gDefaultField.get(null);
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        Field mInstanceField = singletonClass.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        singletonClass.getMethod("get").invoke(singleton);
        Object mDefaultTaskMgr = mInstanceField.get(singleton);
        Object proxy2 = Proxy.newProxyInstance(
                ModuleClassLoader,
                new Class[]{Class.forName("android.app.IActivityTaskManager")},
                new IActivityManagerHandler(mDefaultTaskMgr));
        mInstanceField.set(singleton, proxy2);
    }
}

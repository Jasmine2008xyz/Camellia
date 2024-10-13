package com.luoyu.utils;

import android.app.Activity;
import android.content.Context;
import android.app.ActivityManager;
import de.robv.android.xposed.XposedHelpers;
import java.util.List;
import java.util.Map;

public class ActivityUtil {
  public static void killAppProcess(Context context) {
    // 注意：不能先杀掉主进程，否则逻辑代码无法继续执行，需先杀掉相关进程最后杀掉主进程
    ActivityManager mActivityManager =
        (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> mList = mActivityManager.getRunningAppProcesses();
    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
      if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
        android.os.Process.killProcess(runningAppProcessInfo.pid);
      }
    }
    android.os.Process.killProcess(android.os.Process.myPid());
    System.exit(0);
  }

  public static Activity getTopActivity() {
    try {
      Object ActivityThread =
          XposedHelpers.getStaticObjectField(
              Class.forName("android.app.ActivityThread"), "sCurrentActivityThread");
      Map<?, ?> activities = (Map) XposedHelpers.getObjectField(ActivityThread, "mActivities");
      for (Object activityRecord : activities.values()) {
        boolean isPause = XposedHelpers.getBooleanField(activityRecord, "paused");
        if (!isPause) {
          Activity act = (Activity) XposedHelpers.getObjectField(activityRecord, "activity");
          XRes.addAssetsPath(act);
          return act;
        }
      }
    } catch (Exception e) {
    }
    return null;
  }
}

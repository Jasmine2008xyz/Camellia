package com.luoyu.camellia.utils;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.app.ActivityManager;
import com.luoyu.camellia.base.HookEnv;
import java.util.List;

public class Util {
    public static void SetTextClipboard(String str) {
        ClipboardManager manager =
                (ClipboardManager) HookEnv.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", str);
        manager.setPrimaryClip(data);
        Thread.currentThread();
    }

    public static void PostToMain(Runnable run) {
        new Handler(Looper.getMainLooper()).post(run);
    }

    public static void PostToMainDelay(Runnable run, long delay) {
        new Handler(Looper.getMainLooper()).postDelayed(run, delay);
    }

    public static void restartSelf(Context context) {
        Intent in =
                new Intent(context, ClassUtil.load("com.tencent.mobileqq.activity.SplashActivity"));
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(in);
        ActivityManager mActivityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> mList =
                mActivityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : mList) {
            if (runningAppProcessInfo.pid != android.os.Process.myPid()) {
                android.os.Process.killProcess(runningAppProcessInfo.pid);
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

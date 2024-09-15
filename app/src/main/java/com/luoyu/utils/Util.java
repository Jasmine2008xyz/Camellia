package com.luoyu.utils;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.content.Intent;
import android.app.ActivityManager;
import android.util.Log;
import androidx.annotation.NonNull;
import com.luoyu.xposed.base.HookEnv;
import java.util.List;

public class Util {

    public static String getStackTraceString(Exception e) {
        return Log.getStackTraceString(e);
    }

    public static void setTextClipboard(String str) {
        ClipboardManager manager =
                (ClipboardManager) HookEnv.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", str);
        manager.setPrimaryClip(data);
        Thread.currentThread();
    }

    public static void postToMain(Runnable run) {
        new Handler(Looper.getMainLooper()).post(run);
    }

    public static void postToMainDelay(Runnable run, long delay) {
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

    public static int parseColor(@NonNull String str) {
        return Color.parseColor(str);
    }
    
}

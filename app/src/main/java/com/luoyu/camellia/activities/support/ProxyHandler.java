package com.luoyu.camellia.activities.support;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ProxyHandler implements Handler.Callback {
    private final Handler.Callback mDefault;

    public ProxyHandler(Handler.Callback defaultCallback) {
        mDefault = defaultCallback;
    }

    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        switch (msg.what) {
                // LAUNCH_ACTIVITY     sdk <= 8.0
            case 100 -> {
                try {
                    Object record = msg.obj;
                    Field fIntent = record.getClass().getDeclaredField("intent");
                    fIntent.setAccessible(true);
                    Intent intent = (Intent) fIntent.get(record);
                    assert intent != null;
                    // 获取bundle
                    Bundle bundle = null;
                    try {
                        Field fExtras = Intent.class.getDeclaredField("mExtras");
                        fExtras.setAccessible(true);
                        bundle = (Bundle) fExtras.get(intent);
                    } catch (Exception e) {
                    }
                    // 设置
                    if (bundle != null) {
                        bundle.setClassLoader(ActivityProxyManager.HostClassLoader);
                        if (intent.hasExtra("随便填点，检测用的")) {
                            Intent rIntent = intent.getParcelableExtra("随便填点，检测用的");
                            fIntent.set(record, rIntent);
                        }
                    }
                } catch (Exception e) {
                }
            }
                // EXECUTE_TRANSACTION    8.0+
            case 159 -> {
                Object clientTransaction = msg.obj;
                try {
                    if (clientTransaction != null) {
                        //                        Method getCallbacksMethod =
                        // clientTransaction.getClass().getDeclaredMethod("getCallbacks");
                        Method getCallbacksMethod =
                                Class.forName("android.app.servertransaction.ClientTransaction")
                                        .getDeclaredMethod("getCallbacks");
                        getCallbacksMethod.setAccessible(true);
                        List<?> clientTransactionItems =
                                (List<?>) getCallbacksMethod.invoke(clientTransaction);
                        if (clientTransactionItems == null && clientTransactionItems.isEmpty())
                            break;
                        for (Object item : clientTransactionItems) {
                            Class<?> clz = item.getClass();
                            if (clz.getName().contains("LaunchActivityItem")) {
                                processLaunchActivityItem(clientTransaction, item);
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
                //            default:
                //                LogUtil.log("code -> " + msg.what);
        }
        return mDefault != null && mDefault.handleMessage(msg);
    }

    private static void processLaunchActivityItem(Object clientTransaction, Object item)
            throws NoSuchFieldException,
                    IllegalAccessException,
                    InvocationTargetException,
                    NoSuchMethodException,
                    ClassNotFoundException {
        Class<?> clz = item.getClass();
        Field fmIntent = clz.getDeclaredField("mIntent");
        fmIntent.setAccessible(true);
        Intent wrapper = (Intent) fmIntent.get(item);
        Log.d("ParasiticsUtils:", "handleMessage: target wrapper =" + wrapper);
        assert wrapper != null;
        // 获取Bundle
        Bundle bundle = null;
        try {
            Field fExtras = Intent.class.getDeclaredField("mExtras");
            fExtras.setAccessible(true);
            bundle = (Bundle) fExtras.get(wrapper);
        } catch (Exception e) {
        }
        // 设置
        if (bundle != null) {
            bundle.setClassLoader(ActivityProxyManager.HostClassLoader);
            if (wrapper.hasExtra("随便填点，检测用的")) {
                Intent realIntent = wrapper.getParcelableExtra("随便填点，检测用的");
                fmIntent.set(item, realIntent);
                // android 12
                if (Build.VERSION.SDK_INT >= 31) {
                    IBinder token =
                            (IBinder)
                                    clientTransaction
                                            .getClass()
                                            .getMethod("getActivityToken")
                                            .invoke(clientTransaction);
                    Class<?> cActivityThread = Class.forName("android.app.ActivityThread");
                    Method currentActivityThread =
                            cActivityThread.getDeclaredMethod("currentActivityThread");
                    currentActivityThread.setAccessible(true);
                    Object activityThread = currentActivityThread.invoke(null);
                    assert activityThread != null;
                    try {
                        Object acr =
                                activityThread
                                        .getClass()
                                        .getMethod("getLaunchingActivity", IBinder.class)
                                        .invoke(activityThread, token);
                        if (acr != null) {
                            Field fAcrIntent = acr.getClass().getDeclaredField("intent");
                            fAcrIntent.setAccessible(true);
                            fAcrIntent.set(acr, realIntent);
                        }
                    } catch (NoSuchMethodException e) {
                        if (Build.VERSION.SDK_INT == 33) {
                            // expected behavior...?!
                        } else {
                            throw e;
                        }
                    }
                }
            }
        }
    }
}

package com.luoyu.camellia.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import com.luoyu.xposed.logging.LogCat;
import java.lang.reflect.Method;

public class XRes {
    
    public static void addAssetsPath(Context context) {
        addAssetsPath(context.getResources(), PathUtil.getModuleApkPath());
    }

    @SuppressLint({"PrivateApi", "DiscouragedPrivateApi"})
    public static void addAssetsPath(Resources resources, String str) {
        try {
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            method.setAccessible(true);
            method.invoke(resources.getAssets(), str);
        } catch (Exception unused) {
            LogCat.e("注入资源错误", unused);
        }
    }
}

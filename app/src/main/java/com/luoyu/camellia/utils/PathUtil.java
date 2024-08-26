package com.luoyu.camellia.utils;

import androidx.annotation.NonNull;

public class PathUtil {
    public static String ApkPath;
    public static String ModuleApkPath;
    public static String ApkDataPath;

    public static String getApkPath() {
        return ApkPath;
    }

    public static String getModuleApkPath() {
        return ModuleApkPath;
    }

    public static String getApkDataPath() {
        return ApkDataPath;
    }

    public static void setApkPath(@NonNull String content) {
        ApkPath = content;
    }

    public static void setApkDataPath(@NonNull String content) {
        ApkDataPath = content;
    }

    public static void setModuleApkPath(@NonNull String content) {
        ModuleApkPath = content;
    }
}

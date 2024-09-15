package com.luoyu.utils;

import androidx.annotation.NonNull;

public class PathUtil {
    
  private PathUtil() {
    throw new RuntimeException("No instance for you!");
  }

  public static String apkPath;
  public static String moduleApkPath;
  public static String apkDataPath;

  public static String getApkPath() {
    return apkPath;
  }

  public static String getModuleApkPath() {
    return moduleApkPath;
  }

  public static String getApkDataPath() {
    return apkDataPath;
  }

  public static void setApkPath(@NonNull String content) {
    apkPath = content;
  }

  public static void setApkDataPath(@NonNull String content) {
    apkDataPath = content;
  }

  public static void setModuleApkPath(@NonNull String content) {
    moduleApkPath = content;
  }
}

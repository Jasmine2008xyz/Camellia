package com.luoyu.utils;

import android.content.Context;

public class Hooker {
  /*
   * Wrtiiten by Xiao Ming
   * Date 2024/06/20 12:24
   */

  /*
   * int getResId(Context,String,String);
   */
  public static int getResId(Context context, String resourceName, String resourceType) {
    return context
        .getResources()
        .getIdentifier(resourceType, resourceName, context.getPackageName());
  }

  public static String getStackData() {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
      sb.append(stackTraceElement.toString());
      sb.append("\n");
    }
    return sb.toString();
  }
}

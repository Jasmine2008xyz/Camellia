package com.luoyu.xposed.logging;

import androidx.annotation.NonNull;
import java.io.File;
import com.luoyu.utils.FileUtil;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class LogCat {
  public static String QQLogCatPath;

  public static void setQQLogCatPath(@NonNull String path) {
    QQLogCatPath = path;
  }

  public static String getQQLogCatPath() {
    if (QQLogCatPath != null) return QQLogCatPath;
    else throw new RuntimeException("No QLogPath");
  }

  public static void log(String TAG, Object ex) {
    StringBuilder sb = new StringBuilder(GetNowTime());
    sb.append("\n");
    sb.append(TAG);
    sb.append("\n");
    sb.append(String.valueOf(ex));
    String content = FileUtil.readFileString(QQLogCatPath);
    if (content == null) {
      content = "";
      sb = new StringBuilder(sb.substring(1));
    }
    FileUtil.writeToFile(QQLogCatPath, "" + content + "\n" + sb.toString());
  }

  public static void d(String TAG, Object ex) {
    log(String.format("d-%s", TAG), String.valueOf(ex));
  }

  public static void e(String TAG, Object ex) {
    log(String.format("e-%s", TAG), String.valueOf(ex));
  }

  public static void t(String TAG, Object ex) {
    log(String.format("t-%s", TAG), String.valueOf(ex));
  }

  public static void i(String TAG, Object ex) {
    log(String.format("i-%s", TAG), String.valueOf(ex));
  }

  public static void w(String TAG, Object ex) {
    log(String.format("w-%s", TAG), String.valueOf(ex));
  }

  private static String GetNowTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
    Calendar calendar = Calendar.getInstance();
    String nowtime = sdf.format(calendar.getTime());
    return nowtime;
  }
}

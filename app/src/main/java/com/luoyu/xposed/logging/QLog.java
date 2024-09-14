package com.luoyu.xposed.logging;

import androidx.annotation.NonNull;
import com.luoyu.xposed.logging.IQLog;
import com.luoyu.camellia.utils.FileUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class QLog implements IQLog {
  public String QLogPath;

  @Override
  public void setQLogPath(@NonNull String path) {
    this.QLogPath = path;
  }

  @Override
  public String getQLogPath() {
    if (QLogPath != null) return this.QLogPath;
    else throw new RuntimeException("No QLogPath");
  }

  @Override
  public void log(String TAG, Object ex) {
    File file = new File(QLogPath);
    StringBuilder sb = new StringBuilder(GetNowTime());
    sb.append("\n");
    sb.append(TAG);
    sb.append("\n");
    sb.append(String.valueOf(ex));
    String content = FileUtil.readFileString(QLogPath);
    if (content == null) {
      content = "";
      sb = new StringBuilder(sb.substring(1));
    }
    FileUtil.writeToFile(QLogPath, "" + content + "\n" + sb.toString());
  }

  @Override
  public void d(String TAG, Object ex) {
    log(String.format("d-%s", TAG), String.valueOf(ex));
  }

  @Override
  public void e(String TAG, Object ex) {
    log(String.format("e-%s", TAG), String.valueOf(ex));
  }

  @Override
  public void t(String TAG, Object ex) {
    log(String.format("t-%s", TAG), String.valueOf(ex));
  }

  @Override
  public void i(String TAG, Object ex) {
    log(String.format("i-%s", TAG), String.valueOf(ex));
  }

  @Override
  public void w(String TAG, Object ex) {
    log(String.format("w-%s", TAG), String.valueOf(ex));
  }

  private static String GetNowTime() {
    SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm:ss");
    Calendar calendar = Calendar.getInstance();
    String nowtime = sdf.format(calendar.getTime());
    return nowtime;
  }
}

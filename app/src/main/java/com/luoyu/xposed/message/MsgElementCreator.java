package com.luoyu.xposed.message;

import android.os.Environment;
import com.luoyu.http.HttpSender;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.FileUtil;
import com.luoyu.utils.PathUtil;
import com.luoyu.xposed.base.QRoute;
import com.luoyu.xposed.utils.QQUtil;
import java.lang.reflect.InvocationTargetException;
import de.robv.android.xposed.XposedHelpers;
import java.io.File;
import android.media.MediaMetadataRetriever;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class MsgElementCreator {
  public static Object createTextElement(String text)
      throws ClassNotFoundException,
          InvocationTargetException,
          IllegalAccessException,
          IllegalArgumentException {
    Object o = QRoute.api(ClassUtil.load("com.tencent.qqnt.msg.api.IMsgUtilApi"));
    return XposedHelpers.callMethod(o, "createTextElement", new Class[] {String.class}, text);
  }

  public static Object createAtTextElement(String text, String uin, int atType)
      throws ClassNotFoundException,
          InvocationTargetException,
          IllegalAccessException,
          IllegalArgumentException { // 0不艾特1全体2个人
    Object o = QRoute.api(ClassUtil.load("com.tencent.qqnt.msg.api.IMsgUtilApi"));
    return XposedHelpers.callMethod(
        o,
        "createAtTextElement",
        new Class[] {String.class, String.class, int.class},
        text,
        uin,
        atType);
  }

  public static Object createReplyElement(long msgId)
      throws ClassNotFoundException,
          InvocationTargetException,
          IllegalAccessException,
          IllegalArgumentException {
    Object o = QRoute.api(ClassUtil.get("com.tencent.qqnt.msg.api.IMsgUtilApi"));
    return XposedHelpers.callMethod(o, "createReplyElement", new Class[] {long.class}, msgId);
  }

  public static Object createFileElement(String path)
      throws ClassNotFoundException,
          InvocationTargetException,
          IllegalAccessException,
          IllegalArgumentException {
    Object o = QRoute.api(ClassUtil.get("com.tencent.qqnt.msg.api.IMsgUtilApi"));
    return XposedHelpers.callMethod(o, "createFileElement", new Class[] {String.class}, path);
  }

  public static Object createVideoElement(String path)
      throws ClassNotFoundException,
          InvocationTargetException,
          IllegalAccessException,
          IllegalArgumentException {
    Object o = QRoute.api(ClassUtil.load("com.tencent.qqnt.msg.api.IMsgUtilApi"));
    return XposedHelpers.callMethod(o, "createVideoElement", new Class[] {String.class}, path);
  }

  public static Object createArkElement(String card)
      throws ClassNotFoundException,
          InvocationTargetException,
          IllegalAccessException,
          IllegalArgumentException {
    Class<?> card_data = ClassUtil.load("com.tencent.qqnt.msg.data.b");
    try {
      Object card_data_object = card_data.newInstance();
      boolean o1 =
          (boolean)
              XposedHelpers.callMethod(card_data_object, "o", new Class[] {String.class}, card);
      if (!o1) {
        // Log.d("报错", "卡片格式有问题:" + card);
        // ForLogUtils.Error("createArkElement，卡片格式错误",card);
        return null;
      }
      Object o = QRoute.api(ClassUtil.load("com.tencent.qqnt.msg.api.IMsgUtilApi"));
      return XposedHelpers.callMethod(
          o, "createArkElement", new Class[] {card_data}, card_data_object);
    } catch (IllegalAccessException | InstantiationException e) {

      //	ForLogUtils.Error("createArkElement",e);
      return null;
    }
  }

  public static Object createPttElement(String url)
      throws ClassNotFoundException,
          InvocationTargetException,
          IllegalAccessException,
          IllegalArgumentException {
    String path = url;
    Object o = QRoute.api(ClassUtil.load("com.tencent.qqnt.msg.api.IMsgUtilApi"));
    ArrayList<Byte> myList =
        new ArrayList<>(
            Arrays.asList(
                new Byte[] {
                  28, 26, 43, 29, 31, 61, 34, 49, 51, 56, 52, 74, 41, 62, 66, 46, 25, 57, 51, 70,
                  33, 45, 39, 27, 68, 58, 46, 59, 59, 63
                }));
    return XposedHelpers.callMethod(
        o,
        "createPttElement",
        new Class[] {String.class, int.class, ArrayList.class},
        path,
        (int) getDuration(path),
        myList);
  }

  public static String cachePttPath(String url) {
    String copyTo =
        Environment.getExternalStorageDirectory()
            + "/Android/data/com.tencent.mobileqq/Tencent/MobileQQ/"
            + QQUtil.getCurrentUin()
            + "/ptt/";
    // url
    if (url.toLowerCase().startsWith("http:") || url.toLowerCase().startsWith("https:")) {
      String mRandomPathName = (String.valueOf(Math.random())).substring(2);
      HttpSender.downloadToFileFromQQ(url, copyTo + mRandomPathName + ".aac");
      copyTo += mRandomPathName + ".aac";
    } else {
      copyTo += new File(url).getName();
      try {
        FileUtil.copyFile(url, copyTo);
      } catch (Exception e) {

      }
    }
    return copyTo;
  }

  public static String getModuleCachePath(String dirName) {
    File cache = new File(PathUtil.getApkDataPath() + "Cache/" + dirName);
    if (!cache.exists()) cache.mkdirs();
    return cache.getAbsolutePath();
  }

  /**
   * 获取 视频 或 音频 时长
   *
   * @param path 视频 或 音频 文件路径
   * @return 时长 毫秒值
   */
  public static long getDuration(String path) {
    android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();
    long duration = 0;
    try {
      if (path != null) {
        mmr.setDataSource(path);
      }
      String time = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
      duration = Long.parseLong(time);
    } catch (Exception ignored) {
    } finally {
      try {
        mmr.release();
      } catch (IOException ignored) {
      }
    }
    return duration;
  }
}

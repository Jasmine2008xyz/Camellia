package com.luoyu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.logging.LogCat;

import com.luoyu.xposed.logging.LogCat;
import java.io.File;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author 明月清风与我 @Date 2024/02/10 22:24 @Describe 应用工具
 */
public class AppUtil {
  /**
   * @Method 请求卸载某应用(请注意清单申请权限)
   *
   * @param context 本应用的context
   * @param packageName 要删除的包名 @Return NoReturn
   */
  public static void requestUninstall(Context context, String packageName) {
    Intent intent = new Intent(Intent.ACTION_DELETE);
    intent.setData(Uri.parse("package:" + packageName));
    context.startActivity(intent);
  }

  /**
   * @Method 是否安装某应用(请注意清单申请权限)
   *
   * @param context 本应用的context
   * @param packageName 要检测的包名 @Return boolean 存在/不存在 : true/false
   */
  public static boolean isAppInstalled(Context context, String packageName) {
    PackageManager packageManager = context.getPackageManager();
    try {
      PackageInfo a = packageManager.getPackageInfo(packageName, 0);
      if (a == null) return false;
      else {
        return true;
      }
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * @Method 检测是否开启VPN(请注意清单申请权限) @Param NoParam @Return boolean 开了/没开 : true/false
   */
  public static boolean a() {
    List<String> networkList = new ArrayList<>();
    try {
      for (NetworkInterface networkInterface :
          Collections.list(NetworkInterface.getNetworkInterfaces())) {
        if (networkInterface.isUp()) networkList.add(networkInterface.getName());
      }
    } catch (Exception ex) {

    }
    boolean vpn = networkList.contains("tun0");
    return vpn;
  }

  /**
   * @Method 请求安装某应用(请注意清单申请权限)
   *
   * @param path 安装包路径
   * @param context 活动 @Return NoReturn
   */
  public static void install(String path, Activity context) {
    File apk = new File(path);
    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
      Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + "", apk);
      intent.setDataAndType(uri, "application/vnd.android.package-archive");
    } else {
      intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
    }
    try {
      context.startActivity(intent);
    } catch (Exception e) {
      Toast.makeText(context, "拉起安装错误\n" + e, 0).show();
      //  ToastUtils.Toast(context, "拉起安装错误\n" + e);
      e.printStackTrace();
    }
  }

  /**
   * @Method 打开某应用(请注意清单申请权限)
   *
   * @param context 活动
   * @param packageName 打开应用的包名 @Return NoReturn
   */
  public static void openapp(Activity context, String packageName) {
    try {
      Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
      context.startActivity(intent);
    } catch (Exception t) {
      Toast.makeText(context, "打开错误\n" + t, Toast.LENGTH_SHORT).show();
    }
  }

  /**
   * @Method 获取应用版本(请注意清单申请权限)
   *
   * @param context 本应用的context
   * @param str 要获取应用的包名 @Return String 应用版本
   */
  public static String getAppVersion(Context context, String str) {
    try {
      return context.getPackageManager().getPackageInfo(str, 0).versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * @Method 获取某应用名称(请注意清单申请权限)
   *
   * @param context 本应用的context
   * @param packageName 要删除的包名 @Return String 应用名称
   */
  public static String getAppName(Context context, String str) {
    PackageManager packageManager = context.getPackageManager();
    try {
      return packageManager
          .getPackageInfo(str, 0)
          .applicationInfo
          .loadLabel(packageManager)
          .toString();
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * @Method 获取某应用图标(请注意清单申请权限)
   *
   * @param context 本应用的context
   * @param packageName 要获取应用的包名 @Return Bitmap 应用图标
   */
  public static Bitmap getAppIcon(Context context, String str) {
    PackageManager packageManager = context.getPackageManager();
    try {
      Drawable loadIcon = packageManager.getApplicationInfo(str, 0).loadIcon(packageManager);
      Bitmap createBitmap =
          Bitmap.createBitmap(
              loadIcon.getIntrinsicWidth(), loadIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
      Canvas canvas = new Canvas(createBitmap);
      loadIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
      loadIcon.draw(canvas);
      createBitmap = getPicShape(createBitmap, 180, 180, 50);
      return createBitmap;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * @Method 检测一个字符串是否在于字符串组中
   *
   * @param str 被检测的字符串
   * @param strArr[] 数组 @Return boolean 存在/不存在 : true/false
   */
  public static boolean isStringInArray(String str, String[] strArr) {
    return Arrays.asList(strArr).contains(str);
  }

  /**
   * @Method 字符串组里找字符串位置
   *
   * @param str 字符串
   * @param strArr[] 字符串组 @Return int 位置
   */
  public static int findstrPosition(String str, String[] strArr) {
    for (int i = 0; i < strArr.length; i++) {
      if (strArr[i].equals(str)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @Method 请求Root
   *
   * @param NoParam @Return boolean 已Root/未Root : true/false
   */
  public static boolean isRoot() {
    try {
      Process exec = Runtime.getRuntime().exec("su");
      OutputStream outputStream = exec.getOutputStream();
      outputStream.write("exit\n".getBytes());
      outputStream.flush();
      outputStream.close();
      boolean z = exec.waitFor() == 0;
      exec.destroy();
      return z;
    } catch (Throwable unused) {
      return false;
    }
  }

  /**
   * @Method 获取应用版本名称和版本号 @Param context 上下文 @Return String 版本名字(版本号)
   */
  @SuppressWarnings("deprecation")
  public static String getHostInfo(Context context) {
    try {
      return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
          + "("
          + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode
          + ")";
    } catch (Throwable e) {
      //    ForLogUtils.Error("getHostInfo",Log.getStackTraceString(e));
      throw new AssertionError("Can not get PackageInfo!");
    }
  }

  public static int getVersionNameInt(Context context) {
    try {
      String ver =
          context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName + "";
      ver = ver.replace(".", "");
      return Integer.parseInt(ver);
    } catch (Throwable e) {
      LogCat.log("AppUtils_getVersionNameInt", Log.getStackTraceString(e));
      //    ForLogUtils.Error("getHostInfo",Log.getStackTraceString(e));
      throw new AssertionError("Can not get PackageInfo!");
    }
  }

  @SuppressWarnings("deprecation")
  public static int getVersionCode(Context context) {
    try {
      int ver = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
      return ver;
    } catch (Throwable e) {
      LogCat.e("AppUtils_getVersionCode", Log.getStackTraceString(e));
      //    ForLogUtils.Error("getHostInfo",Log.getStackTraceString(e));
      throw new AssertionError("Can not get PackageInfo!");
    }
  }

  public static Bitmap getPicShape(Bitmap bitmap, int outWidth, int outHeight, int radius) {
    float widthScale = outWidth * 1.0f / bitmap.getWidth();
    float heightScale = outHeight * 1.0f / bitmap.getHeight();
    Matrix matrix = new Matrix();
    matrix.setScale(widthScale, heightScale);
    BitmapShader bitmapShader =
        new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    bitmapShader.setLocalMatrix(matrix);
    Bitmap targetBitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
    Canvas targetCanvas = new Canvas(targetBitmap);
    Paint paint = new Paint();
    paint.setAntiAlias(true);
    paint.setShader(bitmapShader);
    targetCanvas.drawRoundRect(new RectF(0, 0, outWidth, outHeight), radius, radius, paint);
    return targetBitmap;
  }
}

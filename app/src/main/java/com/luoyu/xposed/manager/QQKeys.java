package com.luoyu.xposed.manager;

import com.luoyu.utils.ClassUtil;
import de.robv.android.xposed.XposedHelpers;
import com.luoyu.xposed.utils.QQUtil;

public class QQKeys {
  public static String getSkey() {
    try {
      Object TickManager = GetTicketManager();
      return XposedHelpers.callMethod(
              TickManager, "getSkey", new Class[] {String.class}, QQUtil.getCurrentUin())
          .toString();
      //  MethodUtils.CallMethod(TickManager, "getSkey", String.class, new Class[]{String.class},
      // QQUtil.getCurrentUin()).toString();
    } catch (Exception e) {

      return e.toString();
    }
  }

  public static String getPsKey(String Domain) {
    try {
      Object TickManager = GetTicketManager();
      return XposedHelpers.callMethod(
              TickManager,
              "getPskey",
              new Class[] {String.class, String.class},
              QQUtil.getCurrentUin(),
              Domain)
          .toString();
    } catch (Exception e) {

      return "";
    }
  }

  public static String getG_TK(String Url) {
    String p_skey = getPsKey(Url);
    int hash = 5381;
    for (int i = 0; i < p_skey.length(); ++i) {
      hash += (hash << 5) + p_skey.charAt(i);
    }
    return String.valueOf(hash & 0x7fffffff);
  }

  public static String getSuperKey() {
    try {
      Object TickManager = GetTicketManager();
      return XposedHelpers.callMethod(
              TickManager, "getSuperkey", new Class[] {String.class}, QQUtil.getCurrentUin())
          .toString();
    } catch (Exception e) {

      return "";
    }
  }

  public static String getPt4Token(String Domain) {
    try {
      Object TickManager = GetTicketManager();
      return XposedHelpers.callMethod(
              TickManager,
              "getPt4Token",
              new Class[] {String.class, String.class},
              QQUtil.getCurrentUin(),
              Domain)
          .toString();
    } catch (Exception e) {

      return "";
    }
  }

  public static String getBKN() {
    int hash = 5381;
    String Skey = getSkey();
    byte[] b = Skey.getBytes();
    for (int i = 0, len = b.length; i < len; ++i) hash += (hash << 5) + (int) b[i];
    return "" + (hash & 2147483647);
  }

  public static Object GetTicketManager() throws Exception {
    return XposedHelpers.callMethod(
        QQUtil.getAppRuntime(), "getManager", new Class[] {int.class}, 2);
    //  MethodUtils.CallMethod(QQUtil.getAppRuntime(), "getManager",
    // ClassTool.loadClass("mqq.manager.Manager"), new Class[]{int.class}, 2);
  }
}

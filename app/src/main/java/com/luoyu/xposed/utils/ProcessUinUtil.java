package com.luoyu.xposed.utils;

public class ProcessUinUtil {
  public static String process(String uin) {
    var str = uin.substring(2, uin.length() - 1);
    StringBuilder sb = new StringBuilder();
    for (int i = 2; i < uin.length() - 1; ++i) sb.append("*");

    return uin.replace(str, sb.toString());
  }
}

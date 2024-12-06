package com.luoyu.camellia.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import java.util.List;
import android.content.pm.ResolveInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class IntentUtil {
  public static void openBilibili(Context act) {
    try {
      Toast.makeText(act, "正在尝试升起BiliBili进入主页", Toast.LENGTH_SHORT).show();
      String url = "https://b23.tv/XiciT80";
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(url));
      intent.setPackage("tv.danmaku.bili");
      act.startActivity(intent);
    } catch (Exception e) {
      Toast.makeText(act, "升起BiliBili主页错误：可能是没有安装BiliBili" + e.toString(), Toast.LENGTH_SHORT)
          .show();
    }
  }

  public static void openQQ(Context act, String Uin) {
    Uri u =
        Uri.parse(
            "mqq://card/show_pslcard?src_type=internal&version=1&uin="
                + Uin
                + "&card_type=friend&source=qrcode");
    Intent in = new Intent(Intent.ACTION_VIEW, u);
    in.setPackage("com.tencent.mobileqq");
    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    act.startActivity(in);
  }

  public static void openQQGroup(Context act, String Uin) {
    Uri u =
        Uri.parse(
            "mqq://card/show_pslcard?src_type=internal&version=1&uin="
                + Uin
                + "&card_type=group&source=qrcode");
    Intent in = new Intent(Intent.ACTION_VIEW, u);
    in.setPackage("com.tencent.mobileqq");
    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    act.startActivity(in);
  }

  public static void openGithub(Context act) {
    try {
      Toast.makeText(act, "正在打开github...", Toast.LENGTH_SHORT).show();
      String url = "https://github.com/Jasmine2008xyz/Camellia";
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(url));

      act.startActivity(intent);
    } catch (Exception e) {
      Toast.makeText(act, "升起github错误：" + e.toString(), Toast.LENGTH_SHORT).show();
    }
  }
  public static void openTelegram(Context act) {
    try {
      Toast.makeText(act, "正在打开Telegram...", Toast.LENGTH_SHORT).show();
      String url = "https://t.me/wsy666HD";
      Intent intent = new Intent(Intent.ACTION_VIEW);
      intent.setData(Uri.parse(url));

      act.startActivity(intent);
    } catch (Exception e) {
      Toast.makeText(act, "升起Telegram错误：" + e.toString(), Toast.LENGTH_SHORT).show();
    }
  }
}

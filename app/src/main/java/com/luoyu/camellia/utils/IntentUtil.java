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
            Toast.makeText(act, "正在尝试升起BiliBili进入主页", 0).show();
            String url = "https://b23.tv/XiciT80";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setPackage("tv.danmaku.bili");
            act.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(act, "升起BiliBili主页错误：可能是没有安装BiliBili" + e.toString(), 0).show();
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
}

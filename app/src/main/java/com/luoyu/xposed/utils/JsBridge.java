package com.luoyu.xposed.utils;

import android.webkit.JavascriptInterface;
import com.luoyu.xposed.base.HookEnv;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Objects;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Intent;

public class JsBridge {
  public static String a(String str, String str2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(str2);
        stringBuilder.append("\\d+)");
        Matcher matcher = Pattern.compile(stringBuilder.toString()).matcher(str);
        if (!matcher.find()) {
            return "";
        }
        str = matcher.group(1);
        Objects.requireNonNull(str);
        return str.substring(str2.length());
    }

    @JavascriptInterface
    public void copy(String str) {
        ((ClipboardManager) HookEnv.getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", a(str, "itemId\":")));
    }

    @JavascriptInterface
    public void setBubble(String str) {
        Intent intent = new Intent("theme_set");
        intent.putExtra("type", 2);
        intent.putExtra("bubble_id", a(str, "itemId\":"));
        HookEnv.getContext().sendBroadcast(intent);
    }

    @JavascriptInterface
    public void setTheme(String str) {
        Intent intent = new Intent("theme_set");
        intent.putExtra("type", 1);
        intent.putExtra("theme_id", a(str, "itemId\":"));
        HookEnv.getContext().sendBroadcast(intent);
    }
}

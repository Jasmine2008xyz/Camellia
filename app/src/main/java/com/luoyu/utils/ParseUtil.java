package com.luoyu.utils;

import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

public final class ParseUtil {
    private ParseUtil() {
        throw new RuntimeException("No instance for you!");
    }

    public static int parseColor(String color) {
        return Color.parseColor(color);
    }

    @SuppressWarnings("deprecation")
    public static Spanned parseHtml(String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
}

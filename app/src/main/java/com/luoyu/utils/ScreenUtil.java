package com.luoyu.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.view.WindowManager;
import android.util.DisplayMetrics;
import android.graphics.Rect;

public class ScreenUtil {
    /*
     * This is an example
     * WindowManager windowManager = getWindowManager();
     * int screenWidth = ScreenUtils.getScreenWidth(windowManager);
     * int screenHeight = ScreenUtils.getScreenHeight(windowManager);
     */

    public static int getScreenWidth(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenHeightNoNavigationBar(WindowManager windowManager) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        Rect rect = new Rect();
        windowManager.getDefaultDisplay().getRectSize(rect);
        int statusBarHeight = rect.top;

        return displayMetrics.heightPixels - statusBarHeight;
    }

    public static int dip2px(Context context, float dpValue) {
        if (dpValue > 0) {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dpValue * scale + 0.5f);
        } else {
            float f = -dpValue;
            final float scale = context.getResources().getDisplayMetrics().density;
            return -(int) (f * scale + 0.5f);
        }
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static boolean getDarkModeStatus(Context context) {
        int mode =
                context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static int dip2sp(Context context, float dpValue) {
        final float scale =
                context.getResources().getDisplayMetrics().density
                        / context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}

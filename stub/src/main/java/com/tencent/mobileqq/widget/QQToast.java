package com.tencent.mobileqq.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
// import com.tencent.biz.qui.quitoken.b.a;
import com.tencent.mobileqq.qroute.QRoute;
/*import com.tencent.mobileqq.util.IUIServiceProxy;
import com.tencent.mobileqq.utils.QQTheme;
import com.tencent.qmethod.pandoraex.monitor.DeviceInfoMonitor;
import com.tencent.qphone.base.util.BaseApplication;
import com.tencent.qphone.base.util.QLog;
*/
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/* compiled from: P */
public class QQToast {
    public static final int ICON_DEFAULT = 0;
    public static final int ICON_ERROR = 1;
    public static final int ICON_NONE = -1;
    public static final int ICON_QZONE_DEFAULT = 3;
    public static final int ICON_QZONE_ERROR = 4;
    public static final int ICON_QZONE_SHARE_MOOD = 6;
    public static final int ICON_QZONE_SUCCESS = 5;
    public static final int ICON_SUCCESS = 2;
    public static final int LENGTH_LONG = 1;
    public static final int LENGTH_SHORT = 0;
    public static final String TAG = "QQToast";
    private static boolean mIsNeedShowToast = true;
    private static int sUseCustomToast = 1;
    private static DisplayMetrics systemDisplayMetrics;
    private Context mContext;
    private LayoutInflater mInflater;
    private Resources mResources;
    //  private static boolean sTestSwitch = am.a;
    // private static ToastHandler mHandler = new ToastHandler(Looper.getMainLooper(), (a) null);
    //   private static BlockingQueue<ShowToastMessage> mToastQueue = new LinkedBlockingQueue();
    private Drawable icon = null;
    private int iconWidth = 0;
    private int iconHeight = 0;
    private int mToastType = 0;
    private FrameLayout mRightLayout = null;
    private CharSequence message = null;
    private int mDuration = 0;
    private int mThemeId = 1000;
    boolean mAutoTextSize = false;
    private long mLastShowedTime = 0;
    private boolean isUserTouched = false;

    public QQToast(Context context) {
        throw new RuntimeException("stub");
    }

    public static boolean canUseCustomToast() {
        return sUseCustomToast == 1;
    }

    private int dip2px(float f) {
        DisplayMetrics displayMetrics = systemDisplayMetrics;
        if (displayMetrics == null) {
            displayMetrics = this.mContext.getResources().getDisplayMetrics();
        }
        return (int) ((f * displayMetrics.density) + 0.5f);
    }

    public static int getIconColor(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 4) {
                    return i != 5 ? -15550475 : -7745469;
                }
                return -1;
            }
            return -7745469;
        }
        return -1;
    }

    public static int getIconRes(int i) {
        if (i != 1) {
            if (i != 2) {
                if (i != 4) {
                    return i != 5 ? 2131244988 : 2131245600;
                }
                return 2131244434;
            }
            return 2131245600;
        }
        return 2131244434;
    }

    private void initSystemDisplayMetrics() {
        throw new RuntimeException("stub");
    }

    public static boolean isMX2() {
        return Build.BOARD.contains("mx2");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isRemoveReflectionSwitchOn() {
        throw new RuntimeException("stub");
    }

    public static QQToast makeText(Context context, int i, CharSequence charSequence, int i2) {
        return makeText(context, i, charSequence, i2, 1000);
    }

    private boolean needStatusBarHeightCompat() {
        throw new RuntimeException("stub");
    }

    private void resizeUIBackToNormalMode(View view) {
        if (systemDisplayMetrics == null || view == null) {
            return;
        }
        float f =
                view.getContext().getResources().getDisplayMetrics().density
                        / systemDisplayMetrics.density;
        if (f == 1.0f) {
            return;
        }
        resizeUIBackToNormalMode(view, f);
    }

    public static void setIsNeedShowToast(boolean z) {
        throw new RuntimeException("stub");
    }

    public static void setTestSwitch(boolean z) {
        throw new RuntimeException("stub");
    }

    public static void setUseCustomToast(int i) {
        sUseCustomToast = i;
    }

    public static boolean useIOSLikeUI() {
        throw new RuntimeException("stub");
    }

    public Toast create(int i) {
        return create(i, 2131564961, null);
    }

    public int getStatusBarHeight() {
        try {
            return Resources.getSystem()
                    .getDimensionPixelSize(
                            Resources.getSystem()
                                    .getIdentifier("status_bar_height", "dimen", "android"));
        } catch (Exception unused) {
            return (int) ((this.mResources.getDisplayMetrics().density * 25.0f) + 0.5d);
        }
    }

    public int getTitleBarHeight() {
        try {
            return Resources.getSystem()
                    .getDimensionPixelSize(
                            Resources.getSystem()
                                    .getIdentifier("navigation_bar_height", "dimen", "android"));
        } catch (Exception unused) {
            return (int) ((this.mResources.getDisplayMetrics().density * 44.0f) + 0.5d);
        }
    }

    public boolean isShowing() {
        return System.currentTimeMillis() - this.mLastShowedTime
                        <= (this.mDuration == 0 ? 2000L : 3500L)
                && !this.isUserTouched;
    }

    public void setAutoTextSize() {
        this.mAutoTextSize = true;
    }

    public void setDuration(int i) {
        this.mDuration = i;
    }

    public void setRightLinearlayout(FrameLayout frameLayout) {
        this.mRightLayout = frameLayout;
    }

    public void setThemeId(int i) {
        this.mThemeId = i;
    }

    public void setToastIcon(Drawable drawable) {
        this.icon = drawable;
    }

    public void setToastIconWithSize(Drawable drawable, int i, int i2) {
        this.icon = drawable;
        this.iconWidth = i;
        this.iconHeight = i2;
    }

    public void setToastMsg(CharSequence charSequence) {
        this.message = charSequence;
    }

    public void setType(int i) {
        this.mToastType = i;
    }

    public Toast show() {
        throw new RuntimeException("stub");
    }

   /* public void showByQueue(IToastValidListener iToastValidListener) {
        mToastQueue.add(new ShowToastMessage(this, iToastValidListener));
        mHandler.sendEmptyMessage(1);
        if (QLog.isColorLevel()) {
            QLog.d(TAG, 2, "current queue size is " + mToastQueue.size());
        }
    }*/

    public Toast showTouchableToast(int i, int i2, View.OnTouchListener onTouchListener) {
        throw new RuntimeException("stub");
    }

    public static QQToast makeText(
            Context context, int i, CharSequence charSequence, int i2, int i3) {
        QQToast qQToast = new QQToast(context);
        qQToast.setToastIcon(getIconRes(i));
        qQToast.setType(i);
        qQToast.setToastMsg(charSequence);
        qQToast.setDuration(i2);
        qQToast.setThemeId(i3);
        return qQToast;
    }

    public Toast create(int i, int i2, View.OnTouchListener onTouchListener) {
        throw new RuntimeException("stub");
    }

    public void setToastIcon(int i) {
        setToastIcon(this.mResources.getDrawable(i));
    }

    public void setToastMsg(int i) {
        setToastMsg(this.mResources.getString(i));
    }

    private void resizeUIBackToNormalMode(View view, float f) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginLayoutParams =
                    (ViewGroup.MarginLayoutParams) layoutParams;
            marginLayoutParams.width = Math.round(marginLayoutParams.width / f);
            marginLayoutParams.height = Math.round(marginLayoutParams.height / f);
            marginLayoutParams.leftMargin = Math.round(marginLayoutParams.leftMargin / f);
            marginLayoutParams.rightMargin = Math.round(marginLayoutParams.rightMargin / f);
            marginLayoutParams.topMargin = Math.round(marginLayoutParams.topMargin / f);
            marginLayoutParams.bottomMargin = Math.round(marginLayoutParams.bottomMargin / f);
        }
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTextSize(0, textView.getTextSize() / f);
        }
    }

    public static QQToast makeText(Context context, int i, int i2, int i3) {
        return makeText(context, i, i2, i3, 1000);
    }

    public static QQToast makeText(Context context, int i, int i2, int i3, int i4) {
        QQToast qQToast = new QQToast(context);
        qQToast.setToastIcon(getIconRes(i));
        qQToast.setType(i);
        qQToast.setToastMsg(i2);
        qQToast.setDuration(i3);
        qQToast.setThemeId(i4);
        return qQToast;
    }

    public Toast show(int i) {
        throw new RuntimeException("stub");
    }

    public static QQToast makeText(Context context, CharSequence charSequence, int i) {
        return makeText(context, 0, charSequence, i);
    }

    public static QQToast makeText(Context context, CharSequence charSequence, int i, int i2) {
        return makeText(context, 0, charSequence, i, i2);
    }

    public static QQToast makeText(Context context, int i, int i2) {
        return makeText(context, 0, i, i2);
    }

    public static QQToast makeText(
            Context context, int i, String str, FrameLayout frameLayout, int i2) {
        QQToast qQToast = new QQToast(context);
        qQToast.setToastIcon(getIconRes(i));
        qQToast.setType(i);
        qQToast.setToastMsg(str);
        qQToast.setDuration(i2);
        qQToast.setRightLinearlayout(frameLayout);
        return qQToast;
    }
}

package com.luoyu.camellia.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.luoyu.camellia.annotations.Xposed_Item_Controller;
import com.luoyu.camellia.annotations.Xposed_Item_Entry;
import com.luoyu.camellia.annotations.Xposed_Item_Finder;
import com.luoyu.camellia.interfaces.IDexFinder;
import com.luoyu.camellia.logging.QLog;
import com.luoyu.camellia.startup.DexFinderProcessor;
import com.luoyu.camellia.startup.HookInit;
import com.luoyu.camellia.utils.FileUtil;
import com.luoyu.camellia.utils.PathUtil;
import java.util.List;
import android.widget.LinearLayout;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import android.view.View;
import com.luoyu.camellia.base.HookEnv;
import com.luoyu.camellia.base.MItem;
import com.luoyu.camellia.utils.ClassUtil;

@Xposed_Item_Controller
public class PlusMenuInject {
    public static final String TAG = "PlusMenuInject(加号菜单注入)";
    private static Object ItemCache = null;
    
    @Xposed_Item_Finder
    public void find(IDexFinder finder){
        finder.findMethodsByPathAndUseString(new String[]{TAG},new String[]{"com.tencent.qqnt.aio.menu.ui"},new String[]{"QQCustomMenuItem{title="});
    }

    @Xposed_Item_Entry
    public void start() {
        if (MItem.Config.getBooleanData("模块设置/关闭加号菜单注入入口", false)) return;
        XposedHelpers.findAndHookMethod(
                ClassUtil.load("com.tencent.widget.PopupMenuDialog"),
                "createAndAttachItemsView",
                Activity.class,
                List.class,
                LinearLayout.class,
                boolean.class,
                new XC_MethodHook(100) {
                    @SuppressWarnings({"deprecation", "unchecked"}) // 忽略过时 API 的警告，忽略未经检查的操作警告
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        List mMenu = (List) param.args[1];
                        String title;
                        if (FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign") == null
                                || !FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign")
                                        .equals(HookInit.getSign())) title = "Camellia[未激活]";
                        else title = "Camellia";
                        Object mAddItem =
                                XposedHelpers.findConstructorBestMatch(
                                                ClassUtil.get(
                                                        "com.tencent.widget.PopupMenuDialog$MenuItem"),
                                                new Class[] {
                                                    int.class, String.class, String.class, int.class
                                                })
                                        .newInstance(815, title, "介绍随便摸个鱼啦", 0);
                        Drawable drawable =
                                HookEnv.getContext()
                                        .getResources()
                                        .getDrawable(com.luoyu.camellia.R.drawable.topic);
                        XposedHelpers.findField(mAddItem.getClass(), "drawable")
                                .set(mAddItem, drawable);
                        XposedHelpers.findField(mAddItem.getClass(), "titleColorRes")
                                .set(mAddItem, com.luoyu.camellia.R.color.QQBlue);
                        //  FieldUtils.setField(mAddItem, "drawable", drawable);
                        // FieldUtils.setField(mAddItem, "titleColorRes", R.color.QQBlue);
                        //  FieldUtils.setField(mAddItem,"titleSize",20.0f);
                        ItemCache = mAddItem;
                        mMenu.add(0, mAddItem);
                    }
                });
        XposedHelpers.findAndHookMethod(
                ClassUtil.load("com.tencent.widget.PopupMenuDialog"),
                "onClick",
                View.class,
                new XC_MethodHook() {
                    @SuppressLint("ResourceType")
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        View v = (View) param.args[0];
                        if (v.getId() == 815) {
                            if (FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign") == null
                                    || !FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign")
                                            .equals(HookInit.getSign())) {
                                try {
                                    new DexFinderProcessor();
                                } catch (Exception err) {
                                    MItem.QQLog.e(
                                            "DexFinderProcessor_Construction",
                                            Log.getStackTraceString(err));
                                }
                            }
                        }
                    }
                });
    }
}

package com.luoyu.camellia.startup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import com.luoyu.camellia.BuildConfig;
import com.luoyu.camellia.hook.PlusMenuInject;
import com.luoyu.camellia.logging.QLog;
import com.luoyu.camellia.utils.AppUtil;
import com.luoyu.camellia.utils.FileUtil;
import com.luoyu.camellia.utils.XRes;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import android.app.Activity;
import com.luoyu.camellia.base.HookEnv;
import com.luoyu.camellia.base.MItem;
import com.luoyu.camellia.utils.ClassUtil;
import com.luoyu.camellia.utils.MergeClassLoader;
import com.luoyu.camellia.utils.PathUtil;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.lang.reflect.Field;
import de.robv.android.xposed.XposedBridge;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

public class HookInit {
    public static final AtomicBoolean IsInit = new AtomicBoolean();

    public HookInit(XC_LoadPackage.LoadPackageParam lpparam) {

        if (IsInit.getAndSet(true)) return;

        // Init {@link com.luoyu.camellia.base.HookEnv}
        HookEnv.put("SelfClassLoader", HookInit.class.getClassLoader());
        HookEnv.put("HostClassLoader", lpparam.classLoader);

        // Init {@link com.luoyu.camellia.utils.PathUtil}
        PathUtil.setApkPath(lpparam.appInfo.sourceDir);
        PathUtil.setApkDataPath("/sdcard/Android/data/" + lpparam.packageName + "/Camellia/");

        // Replace self classloader
        replaceSelfClassLoader(lpparam.classLoader);

        // Init {@link com.luoyu.camellia.utils.ClassUtil#InitClassLoader(ClassLoader)}
        ClassUtil.InitClassLoader(lpparam.classLoader);

        // Init {@link com.luoyu.camellia.logging.QLog#setQLogPath(String)}
        MItem.QQLog.setQLogPath(PathUtil.getApkDataPath() + "Log.txt");

        // Start hook operations
        XposedHelpers.findAndHookMethod(
                ClassUtil.load("com.tencent.mobileqq.qfix.QFixApplication"),
                "attachBaseContext",
                Context.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        HookEnv.put("HostContext", (Context) param.args[0]);
                        XRes.addAssetsPath(HookEnv.getContext());
                        
                    }
                });
        XposedHelpers.findAndHookMethod(
                Activity.class,
                "onResume",
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Activity activity = (Activity) param.thisObject;
                        XRes.addAssetsPath(activity);
                        HookEnv.put("HostActivity", activity);
                    }
                });
        

        if (FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign") == null
                || !FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign").equals(getSign())) {
            // Signature not found or signature does not match.
    new PlusMenuInject().start();
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    @SuppressWarnings("JavaReflectionMemberAccess")
    private void replaceSelfClassLoader(ClassLoader cl) {
        if (cl == null) {
            throw new NullPointerException("classLoader canot be null");
        }
        try {
            Field fParent = ClassLoader.class.getDeclaredField("parent");
            fParent.setAccessible(true);
            ClassLoader mine = HookInit.class.getClassLoader();
            ClassLoader curr = (ClassLoader) fParent.get(mine);
            if (curr == null) {
                curr = XposedBridge.class.getClassLoader();
            }
            if (!curr.getClass().getName().equals(MergeClassLoader.class.getName())) {
                fParent.set(mine, new MergeClassLoader(curr, cl));
            }
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("deprecation")
    public static String getSign() {
        return "(QQVersion="
                + AppUtil.getHostInfo(HookEnv.getContext())
                + ",ModuleVersion="
                + BuildConfig.BUILD_TYPE
                + BuildConfig.VERSION_NAME
                + "("
                + BuildConfig.VERSION_CODE
                + "),Msg=云汐退群٩(๑•̀ω•́๑)۶)";
    }
}

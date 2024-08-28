package com.luoyu.camellia.startup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.luoyu.camellia.BuildConfig;
import com.luoyu.camellia.activities.support.ActivityProxyManager;
import com.luoyu.camellia.annotations.Xposed_Item_Controller;
import com.luoyu.camellia.annotations.Xposed_Item_Entry;
import com.luoyu.camellia.base.HookEnv;
import com.luoyu.camellia.base.MItem;
import com.luoyu.camellia.hook.PlusMenuInject;
import com.luoyu.camellia.hook.SettingMenuInject;
import com.luoyu.camellia.utils.AppUtil;
import com.luoyu.camellia.utils.ClassUtil;
import com.luoyu.camellia.utils.FileUtil;
import com.luoyu.camellia.utils.MergeClassLoader;
import com.luoyu.camellia.utils.MethodUtil;
import com.luoyu.camellia.utils.PathUtil;
import com.luoyu.camellia.utils.XRes;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class HookInit {
    public static final AtomicBoolean IsInit = new AtomicBoolean();

    private static final AtomicBoolean IsLoad = new AtomicBoolean();

    public static final HashMap<String, Method> Method_Map = new HashMap<>();

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
                        ActivityProxyManager.initActivityProxyManager(
                                HookEnv.getContext(),
                                PathUtil.getApkPath(),
                                com.luoyu.camellia.R.string.app_name);
                        if (!IsLoad.getAndSet(true)) loadMethods();
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
    }
    @SuppressWarnings("deprecation")
    private void loadMethods() {
        if (FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign") == null
                || !FileUtil.ReadFileString(PathUtil.getApkDataPath() + "Sign").equals(getSign())) {
            // Signature not found or signature does not match.
            try {
                new PlusMenuInject().start();
                new SettingMenuInject().start();
            } catch (Exception err) {
                MItem.QQLog.e("loadMethods", err);
            }
        } else {
            try {
                // 先将QQ类放入HashMap
                JSONObject module_class =
                        new JSONObject(
                                FileUtil.ReadFileString(
                                        PathUtil.getApkDataPath() + "Module_Object_Data"));
                JSONObject qq_class =
                        new JSONObject(
                                FileUtil.ReadFileString(
                                        PathUtil.getApkDataPath() + "QQ_Object_Data"));
                Iterator<String> keys = qq_class.keys();
                while (keys.hasNext()) {
                    try {
                        String key = keys.next(); // 在这里处理每个键
                        JSONObject json = qq_class.getJSONObject(key);
                        Class[] clz = new Class[] {};
                        for (int i = 0; i < (int) json.get("paramsLength"); ++i) {
                            clz[i] = ClassUtil.get((String) json.get("params_" + i));
                        }
                        Method_Map.put(
                                key,
                                XposedHelpers.findMethodBestMatch(
                                        ClassUtil.get((String) json.get("className")),
                                        (String) json.get("methodName"),
                                        clz));
                    } catch (Exception err) {
                        MItem.QQLog.e("HookInit-加载模块项目错误", Log.getStackTraceString(err));
                    }
                }
                Iterator<String> module_keys = module_class.keys();
                while (module_keys.hasNext()) {
                    try {
                        String key = module_keys.next(); // 在这里处理每个键
                        String cl = (String) module_class.get(key);
                        Class<?> clz = HookEnv.getSelfClassLoader().loadClass(cl);
                        for (Method m : clz.getDeclaredMethods()) {
                            
                            if (m.getAnnotation(Xposed_Item_Entry.class) != null) {
                               if(MItem.Config.getBooleanData(m.getDeclaringClass().getAnnotation(Xposed_Item_Controller.class).itemTag()+"/开关",false)||m.getDeclaringClass().getAnnotation(Xposed_Item_Controller.class).isApi()){
                                m.invoke(clz.newInstance());
                                    }
                            }
                        }
                    } catch (Exception err) {
                        MItem.QQLog.e("HookInit-加载模块项目错误", Log.getStackTraceString(err));
                    }
                }
            } catch (Exception err) {
                MItem.QQLog.e("HookInit-加载模块项目错误", Log.getStackTraceString(err));
            }
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

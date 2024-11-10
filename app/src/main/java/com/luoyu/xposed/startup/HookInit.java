package com.luoyu.xposed.startup;

import android.annotation.SuppressLint;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.FileUtil;
import com.luoyu.utils.MergeClassLoader;
import com.luoyu.utils.PathUtil;
import com.luoyu.utils.Reflex;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.core.BaseHook;
import com.luoyu.xposed.hook.crash.LogcatCatcher;
import com.luoyu.xposed.logging.LogCat;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import java.io.File;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

public class HookInit {
    public static final AtomicBoolean IsInit = new AtomicBoolean();

    private static BaseHook baseHook;

    public HookInit(XC_LoadPackage.LoadPackageParam lpparam) {

        if (IsInit.getAndSet(true)) return;

        // Init {@link com.luoyu.camellia.base.HookEnv}
        HookEnv.put("SelfClassLoader", HookInit.class.getClassLoader());
        HookEnv.put("HostClassLoader", lpparam.classLoader);

        // Init processName and packageName
        HookEnv.put("processName", lpparam.processName);
        HookEnv.put("packageName", lpparam.packageName);

        // Init {@link com.luoyu.camellia.utils.PathUtil}
        PathUtil.setApkPath(lpparam.appInfo.sourceDir);
        PathUtil.setApkDataPath("/sdcard/Android/data/" + lpparam.packageName + "/Camellia/");

        // Replace self classloader
        replaceSelfClassLoader(lpparam.classLoader);

        // Init {@link com.luoyu.camellia.utils.ClassUtil#InitClassLoader(ClassLoader)}
        ClassUtil.InitClassLoader(lpparam.classLoader);
    
        // Init {@link com.luoyu.utils.Reflex#setHostClassLoader(ClassLoader)}
        Reflex.setHostClassLoader(lpparam.classLoader);

        // {@link com.luoyu.camellia.logging.QLog#setQLogPath(String)} was Abandoned
        // Init {@link com.luoyu.xposed.logging.LogCat#setQQLogCatPath(String)}
        LogCat.setQQLogCatPath(PathUtil.getApkDataPath() + "Log.txt");

        // Clean up cache
        File file = new File(PathUtil.getApkDataPath() + "cache");
        if (file.exists()) {
            FileUtil.deleteFile(file);
        }
        
        /*
         * Start preventing crash before hook operation.
         */
        if (ModuleController.Config.getBooleanData("阻止Java层闪退/开关", false))
            LogcatCatcher.startCatcherOnce();
            
        
        // Start hook operations
        baseHook = new BaseHook();
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
}

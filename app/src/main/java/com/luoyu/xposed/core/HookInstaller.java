package com.luoyu.xposed.core;

import com.luoyu.camellia.BuildConfig;
import com.luoyu.utils.AppUtil;
import com.luoyu.utils.PathUtil;
import com.luoyu.utils.FileUtil;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.hook.PlusMenuInject;
import com.luoyu.xposed.hook.SettingMenuInject;
import com.luoyu.xposed.logging.LogCat;
import java.util.HashMap;
import org.json.JSONObject;
import java.util.Iterator;
import com.luoyu.utils.ClassUtil;
import de.robv.android.xposed.XposedHelpers;
import android.util.Log;
import com.luoyu.xposed.base.HookEnv;
import java.lang.reflect.Method;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;

/*
 * Hook项目装载器
 */

public class HookInstaller {
  private static final String TAG = "HookInstaller";
  public static final HashMap<String, Method> Method_Map = new HashMap<>();

  @SuppressWarnings("deprecation")
  public HookInstaller() {
    if (FileUtil.readFileString(PathUtil.getApkDataPath() + "Sign") == null
        || !FileUtil.readFileString(PathUtil.getApkDataPath() + "Sign").equals(getSign())) {
      // Signature not found or signature does not match.
      try {
        new PlusMenuInject().start();
        new SettingMenuInject().start();
      } catch (Exception err) {
        LogCat.e("loadMethods", err);
      }
    } else {
      try {
        // 先将QQ类放入HashMap
        JSONObject module_class =
            new JSONObject(
                FileUtil.readFileString(PathUtil.getApkDataPath() + "Module_Object_Data"));
        JSONObject qq_class =
            new JSONObject(FileUtil.readFileString(PathUtil.getApkDataPath() + "QQ_Object_Data"));
        Iterator<String> keys = qq_class.keys();
        while (keys.hasNext()) {
          try {
            String key = keys.next(); // 在这里处理每个键
            JSONObject json = qq_class.getJSONObject(key);
            Class[] clz = new Class[(int) json.get("paramsLength")];
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
            LogCat.e(TAG + "-加载模块项目错误", Log.getStackTraceString(err));
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
                if (ModuleController.Config.getBooleanData(
                        m.getDeclaringClass().getAnnotation(Xposed_Item_Controller.class).itemTag()
                            + "/开关",
                        false)
                    || m.getDeclaringClass().getAnnotation(Xposed_Item_Controller.class).isApi()) {
                  m.invoke(clz.newInstance());
                }
              }
            }
          } catch (Exception err) {
            LogCat.e(TAG + "-加载模块项目错误", Log.getStackTraceString(err));
          }
        }

      } catch (Exception err) {
        LogCat.e(TAG + "-加载模块项目错误", Log.getStackTraceString(err));
      }
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

package com.luoyu.xposed.base;

import android.util.Log;
import android.view.View;

import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick;
import com.luoyu.camellia.utils.FileUtil;
import com.luoyu.camellia.utils.PathUtil;
import com.luoyu.xposed.logging.LogCat;
import java.lang.reflect.Method;
import java.util.Iterator;
import org.json.JSONObject;

public class UiClickHandler {
  @SuppressWarnings({"deprecation", "unchecked"})
  public static void onClick(String text) {

    var module = FileUtil.readFileString(PathUtil.getApkDataPath() + "Module_Object_Data");
    try {
      JSONObject json = new JSONObject(module);
      Iterator<String> it = json.keys();
      while (it.hasNext()) {
        String key = it.next();
        Class clz = HookEnv.getSelfClassLoader().loadClass((String) json.get(key));
        Xposed_Item_Controller con =
            (Xposed_Item_Controller) clz.getAnnotation(Xposed_Item_Controller.class);
        if (con.itemTag().equals(text)) {
          for (Method m : clz.getMethods()) {
            if (m.getAnnotation(Xposed_Item_UiClick.class) != null) {
              m.invoke(clz.newInstance());
            }
          }
        }
      }
    } catch (Exception err) {
      LogCat.e("UiClickHandler", Log.getStackTraceString(err));
    }
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  public static void onSwitchClick(String text) {
    var module = FileUtil.readFileString(PathUtil.getApkDataPath() + "Module_Object_Data");
    try {
      JSONObject json = new JSONObject(module);
      Iterator<String> it = json.keys();
      while (it.hasNext()) {
        String key = it.next();
        Class clz = HookEnv.getSelfClassLoader().loadClass((String) json.get(key));
        Xposed_Item_Controller con =
            (Xposed_Item_Controller) clz.getAnnotation(Xposed_Item_Controller.class);
        if (con.itemTag().equals(text)) {
          for (Method m : clz.getMethods()) {
            if (m.getAnnotation(Xposed_Item_Entry.class) != null) {
              m.invoke(clz.newInstance());
            }
          }
        }
      }
    } catch (Exception err) {
      LogCat.e("UiClickHandler", Log.getStackTraceString(err));
    }
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  public static void onLongClick(String text) {
    var module = FileUtil.readFileString(PathUtil.getApkDataPath() + "Module_Object_Data");
    try {
      JSONObject json = new JSONObject(module);
      Iterator<String> it = json.keys();
      while (it.hasNext()) {
        String key = it.next();
        Class clz = HookEnv.getSelfClassLoader().loadClass((String) json.get(key));
        Xposed_Item_Controller con =
            (Xposed_Item_Controller) clz.getAnnotation(Xposed_Item_Controller.class);
        if (con.itemTag().equals(text)) {
          for (Method m : clz.getMethods()) {
            if (m.getAnnotation(Xposed_Item_UiLongClick.class) != null) {
              m.invoke(clz.newInstance());
            }
          }
        }
      }
    } catch (Exception err) {
      LogCat.e("UiClickHandler", Log.getStackTraceString(err));
    }
  }
}

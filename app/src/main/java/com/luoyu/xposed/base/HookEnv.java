package com.luoyu.xposed.base;

import androidx.annotation.NonNull;
import com.luoyu.utils.ActivityUtil;
import java.util.HashMap;
import android.content.Context;
import android.app.Activity;

public class HookEnv {

  public static HashMap<String, Object> HookEnv = new HashMap<>();

  public static <Any> void put(@NonNull String need, Any value) {
    HookEnv.put(need, value);
  }

  @SuppressWarnings("unchecked")
  public static <T> T get(@NonNull String need) {
    if (HookEnv.containsKey(need)) return (T) HookEnv.get(need);
    return null;
  }

  public static Context getContext() {
    return get("HostContext");
  }

  public static Activity getActivity() {
    Activity act = get("HostActivity");
    if (act != null) return act;
    return ActivityUtil.getTopActivity();
  }

  public static ClassLoader getHostClassLoader() {
    return get("HostClassLoader");
  }

  public static ClassLoader getSelfClassLoader() {
    return get("SelfClassLoader");
  }

  public static Object getAppInterFace() {
    return get("AppInterFace");
  }
}

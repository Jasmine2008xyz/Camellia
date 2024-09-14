package com.luoyu.xposed.base;

import androidx.annotation.NonNull;
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
    if (HookEnv.containsKey(need)) {
      return (T) HookEnv.get(need);
    }
    throw null;
  }

  public static Context getContext() {
    return get("HostContext");
  }

  public static Activity getActivity() {
    return get("HostActivity");
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

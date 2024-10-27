package com.luoyu.xposed.hook.voicepanel;

import android.app.Activity;
import android.widget.Toast;
import com.luoyu.utils.ClassUtil;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.logging.LogCat;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import android.widget.ImageView;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import android.os.Bundle;
import de.robv.android.xposed.XposedHelpers;

@Xposed_Item_Controller(itemTag = "语音面板")
public class VoicePanel_Entry {
  private void startHook() {
    try {
      Method method = null;
      Method[] methodArray =
          ClassUtil.get("com.tencent.qqnt.aio.shortcutbar.PanelIconLinearLayout")
              .getDeclaredMethods();
      for (Method m : methodArray) {
        if (m.getReturnType().equals(ImageView.class)) {
          method = m;
        }
      }
      XposedBridge.hookMethod(
          method,
          new XC_MethodHook(50) {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
              ImageView imageView = (ImageView) param.getResult();
              if ("语音".contentEquals(imageView.getContentDescription())) {
                imageView.setOnLongClickListener(
                    view -> {
                      Main_VoicePanel.create(HookEnv.getActivity());
                      return true;
                    });
              }
            }
          });
    } catch (Exception e) {

    }
  }

  @Xposed_Item_Entry
  public void init() {
    try {
      Class<?> clazz = ClassUtil.load("com.tencent.mobileqq.activity.SplashActivity");
      XposedHelpers.findAndHookMethod(
          clazz,
          "doOnCreate",
          Bundle.class,
          new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
              startHook();
            }
          });
    } catch (Exception e) {

    }
  }
}

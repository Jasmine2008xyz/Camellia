package com.luoyu.xposed.hook;

import android.widget.TextView;
import com.luoyu.utils.Reflex;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;

@Xposed_Item_Controller(itemTag = "屏蔽卡屏文本")
public class FuckLargeText {
  @Xposed_Item_Entry
  public void hook() {
    XposedBridge.hookMethod_Before(
        Reflex.findMethod(TextView.class).setMethodName("setText").get(),
        param -> {
          if (param.args[0] instanceof CharSequence) {
            CharSequence msg = (CharSequence) param.args[0];
            if (msg == null) return;
            if (msg.length() > 3000) {
              param.args[0] = "检测到卡屏文本，已进行屏蔽";
            }
          }
        });
  }
}

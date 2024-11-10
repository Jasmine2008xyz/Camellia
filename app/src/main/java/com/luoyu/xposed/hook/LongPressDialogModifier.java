package com.luoyu.xposed.hook;

import android.content.Context;
import android.widget.TextView;
import com.luoyu.utils.ActivityUtil;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

@Xposed_Item_Controller(itemTag = "长按文本修改内容(慎)")
public class LongPressDialogModifier {
  @Xposed_Item_Entry
  public void work() {
    XposedBridge.hookAllConstructors(
        TextView.class,
        new XC_MethodHook() {
          @Override
          public void afterHookedMethod(MethodHookParam param) {
          boolean boo =ModuleController.Config.getBooleanData("长按文本修改内容(慎)/开关", false);
          if(!boo) return;
            ((TextView) param.thisObject)
                .setOnLongClickListener(
                    v -> {
                      // 确保使用的上下文是有效的
                      Context context = ActivityUtil.getTopActivity();
                      if (context != null) {
                        new XPopup.Builder(context)
                            .isDestroyOnDismiss(true)
                            .asInputConfirm(
                                "长按文本修改内容",
                                null,
                               ((TextView)param.thisObject).getText(),
                                "输入要修改成的内容",
                                new OnInputConfirmListener() {
                                  @Override
                                  public void onConfirm(String text) {
                                    ((TextView) param.thisObject).setText(text);
                                  }
                                })
                            .show();
                        return true;
                      }
                      return false;
                    });
          }
        });
  }
}

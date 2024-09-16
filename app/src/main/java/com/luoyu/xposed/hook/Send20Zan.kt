/*package com.luoyu.xposed.hook;

import android.view.View;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.MethodUtil;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Method;

@Xposed_Item_Controller(itemTag = "一键20赞")
public class Send20Zan {
  @Xposed_Item_Entry
  public void hook() {
    Class clazz1 = ClassUtil.load("com.tencent.mobileqq.activity.VisitorsActivity");
    String name1 = "onClick";
    Method method1 =
        MethodUtil.create(clazz1).setMethodName(name1).get(HookEnv.getHostClassLoader());
    XposedBridge.hookMethod(
        method1,
        new XC_MethodHook() {
          @Override
          public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam)
              throws Throwable {
            super.beforeHookedMethod(methodHookParam);
            View view = (View) methodHookParam.args[0];
            if (view.getContentDescription() == null
                || !view.getContentDescription().toString().equals("赞")) return;
            if (ModuleController.Config.getBooleanData("一键20赞/开关", false)) {
              methodHookParam.setResult((Object) null);
              for (int i = 0; i < 20; i++) {
                try {
                  XposedBridge.invokeOriginalMethod(
                      methodHookParam.method, methodHookParam.thisObject, methodHookParam.args);
                } catch (Exception e) {
                  // ForLogUtils.Error("点赞", e);
                }
              }
            }
          }
        });
    Class clazz2 =
        ClassUtil.load("com.tencent.mobileqq.profilecard.base.component.AbsProfileHeaderComponent");
    String name2 = "handleVoteBtnClickForGuestProfile";

    Method method2 =
        XposedHelpers.findMethodBestMatch(
            clazz2,
            "handleVoteBtnClickForGuestProfile",
            ClassUtil.load("com.tencent.mobileqq.data.Card"));

    XposedBridge.hookMethod(
        method2,
        new XC_MethodHook() {
          @Override
          public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam)
              throws Throwable {
            super.beforeHookedMethod(methodHookParam);
            if (ModuleController.Config.getBooleanData("一键20赞/开关", false)) {
              for (int i = 0; i < 20; i++) {
                XposedBridge.invokeOriginalMethod(
                    methodHookParam.method, methodHookParam.thisObject, methodHookParam.args);
              }
            }
          }
        });
  }
}
*/
package com.luoyu.xposed.hook

import android.view.View
import com.luoyu.utils.ClassUtil
import com.luoyu.utils.MethodUtil
import com.luoyu.xposed.ModuleController
import com.luoyu.xposed.base.HookEnv
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Method

@Xposed_Item_Controller(itemTag = "一键20赞")
class Send20Zan {

    @Xposed_Item_Entry
    fun hook() {
        val clazz1 = ClassUtil.load("com.tencent.mobileqq.activity.VisitorsActivity")
        val method1 = MethodUtil.create(clazz1)
            .setMethodName("onClick")
            .get(HookEnv.getHostClassLoader())

        XposedBridge.hookMethod(method1, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                val view = param.args[0] as View
                if (view.contentDescription == null || view.contentDescription.toString() != "赞") return
                if (ModuleController.Config.getBooleanData("一键20赞/开关", false)) {
                    param.result = null
                    repeat(20) {
                        try {
                            XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args)
                        } catch (e: Exception) {
                            // ForLogUtils.Error("点赞", e)
                        }
                   }
                }
            }
        })

        val clazz2 = ClassUtil.load("com.tencent.mobileqq.profilecard.base.component.AbsProfileHeaderComponent")
        val method2 = /*MethodUtil.create(clazz2)
            .setMethodName("handleVoteBtnClickForGuestProfile")
           // .setParams(ClassUtil.load("com.tencent.mobileqq.data.Card"))
            .get(HookEnv.getHostClassLoader())*/
            XposedHelpers.findMethodBestMatch(clazz2,"handleVoteBtnClickForGuestProfile",ClassUtil.load("com.tencent.mobileqq.data.Card"))

        XposedBridge.hookMethod(method2, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                if (ModuleController.Config.getBooleanData("一键20赞/开关", false)) {
                    repeat(20) {
                        XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args)
                    }
                }
            }
        })
    }
}

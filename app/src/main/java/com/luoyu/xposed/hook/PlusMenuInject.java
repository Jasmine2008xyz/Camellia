package com.luoyu.xposed.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.luoyu.camellia.activities.SettingsActivity;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.FileUtil;
import com.luoyu.utils.PathUtil;
import com.luoyu.utils.XposedBridge;
import com.luoyu.utils.temp.MField;
import com.luoyu.utils.temp.MMethod;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.core.HookInstaller;
import com.luoyu.xposed.hook.redpacket.OpenRedPacket;
import com.luoyu.xposed.logging.LogCat;
import com.luoyu.dexfinder.DexFinderProcessor;
import com.luoyu.xposed.startup.HookInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.util.List;

@Xposed_Item_Controller(isApi = true)
public class PlusMenuInject {
  public static final String TAG = "PlusMenuInject(加号菜单注入)";
  private static Object ItemCache = null;

  /*   @Xposed_Item_Finder
  public void find(IDexFinder finder) {
      finder.findMethodsByPathAndUseString(
              new String[] {TAG},
              new String[] {"com.tencent.qqnt.aio.menu.ui"},
              new String[] {"QQCustomMenuItem{title="});
  }*/

  @Xposed_Item_Entry
  public void start() {
    if (ModuleController.Config.getBooleanData("模块设置/关闭加号菜单注入入口", false)) return;
    XposedHelpers.findAndHookMethod(
        ClassUtil.load("com.tencent.widget.PopupMenuDialog"),
        "createAndAttachItemsView",
        Activity.class,
        List.class,
        LinearLayout.class,
        boolean.class,
        new XC_MethodHook(100) {
          @SuppressWarnings({"deprecation", "unchecked"}) // 忽略过时 API 的警告，忽略未经检查的操作警告
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            List mMenu = (List) param.args[1];
            String title;
            if (FileUtil.readFileString(PathUtil.getApkDataPath() + "Sign") == null
                || !FileUtil.readFileString(PathUtil.getApkDataPath() + "Sign")
                    .equals(HookInstaller.getSign())) title = "Camellia[未激活]";
            else title = "Camellia";
            Object mAddItem =
                XposedHelpers.findConstructorBestMatch(
                        ClassUtil.get("com.tencent.widget.PopupMenuDialog$MenuItem"),
                        new Class[] {int.class, String.class, String.class, int.class})
                    .newInstance(815, title, "介绍随便摸个鱼啦", com.luoyu.camellia.R.drawable.topic);
           /* Drawable drawable =
                HookEnv.getContext()
                    .getResources()
                    .getDrawable(com.luoyu.camellia.R.drawable.topic);
            XposedHelpers.findField(mAddItem.getClass(), "drawable").set(mAddItem, drawable);*/
            XposedHelpers.findField(mAddItem.getClass(), "titleColorRes")
                .set(mAddItem, com.luoyu.camellia.R.color.QQBlue);
            //  FieldUtils.setField(mAddItem, "drawable", drawable);
            // FieldUtils.setField(mAddItem, "titleColorRes", R.color.QQBlue);
            //  FieldUtils.setField(mAddItem,"titleSize",20.0f);
            ItemCache = mAddItem;
            mMenu.add(0, mAddItem);
          }
        });
    XposedHelpers.findAndHookMethod(
        ClassUtil.load("com.tencent.widget.PopupMenuDialog"),
        "onClick",
        View.class,
        new XC_MethodHook() {
          @SuppressLint("ResourceType")
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
            View v = (View) param.args[0];
            if (v.getId() == 815) {
              if (FileUtil.readFileString(PathUtil.getApkDataPath() + "Sign") == null
                  || !FileUtil.readFileString(PathUtil.getApkDataPath() + "Sign")
                      .equals(HookInstaller.getSign())) {
                try {
                  new DexFinderProcessor();
                } catch (Exception err) {
                  LogCat.e("DexFinderProcessor_Construction", Log.getStackTraceString(err));
                }
              } else {
                HookEnv.getActivity()
                    .startActivity(new Intent(HookEnv.getActivity(), SettingsActivity.class));
              }
            }
          }
        });

    XposedBridge.hookMethod_After(
        XposedHelpers.findMethodBestMatch(
            ClassUtil.get("com.tenpay.sdk.basebl.EncryptRequest"),
            "encypt",
            new Class[] {
              String.class, String.class, int.class, String.class, String.class, String.class
            }),
        param -> {
         // LogCat.d("调试", "阿巴" + param.args[5]);
                LogCat.d("获取参数", "参数0:"+param.args[0]+"参数1:"+param.args[1]+"参数2:"+param.args[2]+"参数3"+param.args[3]+"参数4:"+param.args[4]+"参数5:"+param.args[5]);
          OpenRedPacket.str5 = param.args[5].toString();
        });
  /*  XposedHelpers.findAndHookConstructor(
        ClassUtil.load("tencent.im.qqwallet.QWalletHbPreGrab$QQHBRequest"),
        new XC_MethodHook() {
          @Override
          protected void afterHookedMethod(MethodHookParam param) throws Throwable {
            super.afterHookedMethod(param);
                    LogCat.d("调试",XposedHelpers.callMethod(MField.GetField(param.thisObject,"reqBody"),"toString","UTF-8"));
          }
        });
    /*    XposedHelpers.findAndHookConstructor(ClassUtil.get("mqq.app.NewIntent"),Context.class,Class.class,new XC_MethodHook()
      {
           @Override
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
      super.beforeHookedMethod(param);
              LogCat.d("获取class",""+param.args[1]);
              }
      });*/
  }
}

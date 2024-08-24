package com.luoyu.camellia.startup;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.luoyu.camellia.annotations.Item_Version_Controller;
import com.luoyu.camellia.annotations.Xposed_Item_Controller;
import com.luoyu.camellia.annotations.Xposed_Item_Finder;
import com.luoyu.camellia.base.HookEnv;
import com.luoyu.camellia.base.MItem;
import com.luoyu.camellia.interfaces.IDexFinder;
import com.luoyu.camellia.ui.widget.RDialog;
import com.luoyu.camellia.R;
import com.luoyu.camellia.utils.ActivityUtil;
import com.luoyu.camellia.utils.AppUtil;
import com.luoyu.camellia.utils.FileUtil;
import com.luoyu.camellia.utils.PathUtil;
import com.luoyu.camellia.utils.Util;
import com.luoyu.dexfinder.DexKitFinder;
import java.lang.reflect.Method;

public class DexFinderProcessor {
    private static final String TAG = "DexFinderProcessor";
    private TextView textMsg;

    public DexFinderProcessor() {
        RDialog dialog = new RDialog(HookEnv.getActivity());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        TextView textview = dialog.root.findViewById(R.id.RDialog_TextView);
        textview.setText("初始化");
        Button btn1 = dialog.root.findViewById(R.id.RDialog_Button);
        btn1.setOnClickListener(
                v -> {
                    dialog.dismiss();
                });
        var btn2 = new Button(dialog.getContext());
        btn2.setBackgroundResource(R.drawable.item_button);
        btn2.setText("开始查找所需对象");
        btn2.setOnClickListener(
                v -> {
                    dialog.container.removeAllViews();
                    ((LinearLayout) dialog.root.findViewById(R.id.RDialog_Button).getParent())
                            .removeAllViews();
                    this.textMsg = new TextView(v.getContext());
                    this.textMsg.setTextColor(Util.parseColor("#1A1A1A"));
                    this.textMsg.setTypeface(null, Typeface.BOLD);
                    this.textMsg.setTextSize(16);
                    this.textMsg.setText("-> 正在准备查找...");
                    dialog.container.addView(textMsg);
                    new Thread(
                                    () -> {
                                        startfindMethod();
                                    })
                            .start();
                });
        dialog.container.addView(btn2);
        dialog.show();
    }

    private void startfindMethod() {
        try {
            sendMsg("   Success\n-> 正在创建查找器...");
            IDexFinder module_finder =
                    new DexKitFinder(HookEnv.getSelfClassLoader(), PathUtil.getModuleApkPath());
            IDexFinder qq_finder =
                    new DexKitFinder(HookEnv.getHostClassLoader(), PathUtil.getApkPath());
            sendMsg("   Success\n-> 正在查找所需对象...");
            Thread.sleep(1000);
            module_finder.findSelfClassesByUseAnnotation(Xposed_Item_Controller.class);
            module_finder
                    .getModule_Object_List()
                    .forEach(
                            clz -> {
                                for (Method m : clz.getMethods()) {
                                    if (m.getAnnotation(Xposed_Item_Finder.class) != null) {
                                        // 检验 Item_Version_Controller
                                        Item_Version_Controller con =
                                                m.getAnnotation(Item_Version_Controller.class);
                                        if (con != null) {
                                            if (AppUtil.getVersionCode(HookEnv.getContext())
                                                            > con.max()
                                                    && con.max() != -1) return;
                                            if (AppUtil.getVersionCode(HookEnv.getContext())
                                                            < con.min()
                                                    && con.min() != -1) return;
                                        }
                                        try {
                                            m.invoke(
                                                    m.getDeclaringClass().newInstance(), qq_finder);
                                        } catch (Exception err) {
                                            MItem.QQLog.e(
                                                    DexFinderProcessor.class.getName(),
                                                    Log.getStackTraceString(err));
                                        }
                                    }
                                }
                            });
            sendMsg("   Success\n-> 查找完成，正在保存并重启QQ...");
            Thread.sleep(1000);
            module_finder.keepToFile();
            qq_finder.keepToFile();
            FileUtil.WriteToFile(PathUtil.getApkDataPath()+"Sign",HookInit.getSign());
            ActivityUtil.killAppProcess(HookEnv.getContext());
        } catch (Exception err) {
            MItem.QQLog.e("startfindMethod()", Log.getStackTraceString(err));
        }
    }

    private void sendMsg(@NonNull String msg) {
        Util.postToMain(
                () -> {
                    this.textMsg.setText(textMsg.getText() + msg);
                });
    }
}

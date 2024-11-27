package com.luoyu.xposed.hook.crash;

import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luoyu.camellia.activities.helper.ActivityAttributes;
import com.luoyu.utils.ParseUtil;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

@Xposed_Item_Controller(isApi = false, itemTag = "阻止Java层闪退")
public class PreventCrash_Entry {
    @Xposed_Item_UiLongClick
    public void onLongClick() {
        MaterialAlertDialogBuilder dialog =
                new MaterialAlertDialogBuilder(ActivityAttributes.context);
        TextView tv = new TextView(ActivityAttributes.context);
        LinearLayout root = new LinearLayout(ActivityAttributes.context);
        root.setGravity(Gravity.CENTER);
        Switch switzh = new Switch(ActivityAttributes.context);
        tv.setText("是否弹窗/打印日志");
        tv.setTextSize(20);
        tv.setTextColor(ParseUtil.parseColor("#1A1A1A"));
        root.setOrientation(LinearLayout.HORIZONTAL);
        switzh.setChecked(ModuleController.Config.getBooleanData("阻止Java层闪退/打印日志", false));

        switzh.setOnCheckedChangeListener(
                (ch, boo) -> {
                    ModuleController.Config.putData("阻止Java层闪退/打印日志", boo);
                    Toast.makeText(ActivityAttributes.context, "设置成功！", Toast.LENGTH_LONG).show();
                });
        root.addView(tv);
        root.addView(switzh);
        dialog.setView(root);
        dialog.setTitle("阻止Java层闪退");
        dialog.setPositiveButton("Leave", null);
        dialog.show();
       try {
    	
   
    XposedBridge.hookMethod(Class.class.getDeclaredMethod("forName",String.class),new XC_MethodHook(){
      @Override
        protected void afterHookedMethod(MethodHookParam param) {
            if(param.getResult()!=null){
        	if(((Class)param.getResult()).getName().startsWith("com.tencent.qqnt")) {
        		XposedBridge.log("Loaded class: " + param.getResult());
        	}
        }}
    });
 } catch(Exception err) {
    	
    }
    }
}

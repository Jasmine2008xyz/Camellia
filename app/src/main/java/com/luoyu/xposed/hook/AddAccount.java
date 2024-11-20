package com.luoyu.xposed.hook;

import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luoyu.camellia.activities.helper.ActivityAttributes;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiClick;
import java.io.File;
import java.io.IOException;

@Xposed_Item_Controller(itemTag = "添加账号")
public class AddAccount {
  @Xposed_Item_UiClick
  public void show() {
    var act = ActivityAttributes.context;
    MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(act);
    dialog.setTitle("添加账号(娱乐功能)");
    final EditText et = new EditText(act);
    et.setHint("请在此输入要添加的账号");
    dialog.setNegativeButton("取消", null);
    dialog.setPositiveButton(
        "保存",
        (dia, which) -> {
          String input = et.getText().toString();
          long qquin = Long.parseLong(input);
          if (qquin < 10000) {
            Toast.makeText(act, "不合法的QQ号", 0).show();
          } else {
            File f = new File(HookEnv.getContext().getFilesDir(), "user/u_" + qquin + "_t");
            try {
              boolean success = f.createNewFile();
            } catch (IOException e) {

            }
          }
        });
    dialog.setView(et);
    dialog.show();
  }
}

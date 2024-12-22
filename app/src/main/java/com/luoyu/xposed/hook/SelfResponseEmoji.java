package com.luoyu.xposed.hook;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luoyu.utils.Reflex;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick;
import de.robv.android.xposed.XposedBridge;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Xposed_Item_Controller(itemTag = "自身回应")
public class SelfResponseEmoji {
  /*
   *                    _ooOoo_
   *                   o8888888o
   *                   88" . "88
   *                   (| -_- |)
   *                    O\ = /O
   *                ____/`---'\____
   *              .   ' \\| |// `.
   *               / \\||| : |||// \
   *             / _||||| -:- |||||- \
   *               | | \\\ - /// | |
   *             | \_| ''\---/'' | |
   *              \ .-\__ `-` ___/-. /
   *           ___`. .' /--.--\ `. . __
   *        ."" '< `.___\_<|>_/___.' >'"".
   *       | | : `- \`.;`\ _ /`;.`/ - ` : | |
   *         \ \ `-. \_ __\ /__ _/ .-` / /
   * ======`-.____`-.___\_____/___.-`____.-'======
   *                    `=---='
   *
   * .............................................
   *          佛祖保佑             永无BUG
   */
  public void loadResponse(Object msgRecord) {
    new Thread(
            () -> {
              try {
                /* Object weakReference =
                Reflex.loadClass("Lmqq/util/WeakReference;")
                    .getConstructor(Object.class)
                    .newInstance(this);*/
                WeakReference weakReference = new WeakReference(Reflex.loadClass("Lcom/tencent/mobileqq/aio/msglist/holder/component/avatar/AIOAvatarContentComponent;").getConstructor(Context.class).newInstance(HookEnv.getContext()));
                Object aioAdapter =
                    Reflex.loadClass(
                            "Lcom/tencent/mobileqq/aio/msglist/holder/component/msgtail/ui/AIOEmoReplyAdapter;")
                        .getConstructor(weakReference.getClass())
                        .newInstance(weakReference);
                Method m = Reflex.findMethod(aioAdapter.getClass()).setParamsLength(3).get();
                /* int id[] =
                new int[] {
                  352, 5, 325, 427, 96, 97, 27, 34, 187, 323, 272, 283, 262, 177, 281, 277
                };*/
                String id[] = ModuleController.Config.getStringData("表情回应", "").split(",");
                for (String i : id) {
                  m.invoke(aioAdapter, Integer.parseInt(i), 1, msgRecord);
                  Thread.sleep(100);
                  System.gc();
                }
              } catch (Exception err) {
                XposedBridge.log("自身回应：" + Log.getStackTraceString(err));
              }
            })
        .start();
  }

  @Xposed_Item_UiLongClick
  public void onClick() {
    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(HookEnv.getActivity());
    builder.setTitle("自定义回应");
    EditText edit = new EditText(builder.getContext());

    edit.setText("" + ModuleController.Config.getData("表情回应", ""));
    builder.setView(edit);
    builder.setPositiveButton(
        "Keep",
        (dia, which) -> {
          ModuleController.Config.putData("表情回应", edit.getText().toString());
        });
    builder.setNegativeButton("Leave", null);
    builder.setNeutralButton(
        "默认方案",
        (dia, which) -> {
          ModuleController.Config.putData(
              "表情回应", "352,96,97,34,323,5,325,262,427,272,177,277,281,27,187,283");
        });
    builder.show();
  }
}

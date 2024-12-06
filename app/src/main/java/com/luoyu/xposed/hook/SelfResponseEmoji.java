package com.luoyu.xposed.hook;

import android.util.Log;
import com.luoyu.utils.Reflex;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import de.robv.android.xposed.XposedBridge;
import java.lang.ref.WeakReference;

@Xposed_Item_Controller(itemTag = "自身回应")
public class SelfResponseEmoji {
  public void loadResponse(Object msgRecord) {
    try {
     /* Object weakReference =
          Reflex.loadClass("Lmqq/util/WeakReference;")
              .getConstructor(Object.class)
              .newInstance(this);*/
      WeakReference weakReference = new WeakReference(null);
      Object aioAdapter =
          Reflex.loadClass(
                  "Lcom/tencent/mobileqq/aio/msglist/holder/component/msgtail/ui/AIOEmoReplyAdapter;")
              .getConstructor(weakReference.getClass())
              .newInstance(weakReference);
      Reflex.findMethod(aioAdapter.getClass())
          .setParamsLength(3)
          .get()
          .invoke(aioAdapter, 277, 1, msgRecord);
    } catch (Exception err) {
XposedBridge.log("自身回应："+Log.getStackTraceString(err));
    }
  }
}

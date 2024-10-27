package com.luoyu.xposed.hook;

import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.logging.LogCat;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import java.util.ArrayList;

@Deprecated
@Xposed_Item_Controller(itemTag = "屏蔽回复表情展示")
public class FuckShowReplyFace {
  @Xposed_Item_Entry
  public void init() {
    XposedBridge.hookMethod_Before(
        XposedHelpers.findMethodBestMatch(
            ClassUtil.get("com.tencent.qqnt.aio.adapter.api.impl.AIOEmoReplyApiImpl"),
            "setEmojiLikes",
            ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.MsgRecord"),
            String.class,
            int.class,
            boolean.class,
            ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.ISetMsgEmojiLikesCallback")),
        param -> {
          //
           LogCat.d("测试","参数0"+param.args[0]+"参数1"+param.args[1]+"参数2"+param.args[2]+"参数3"+param.args[3]+"参数4"+param.args[4]);
     /*     ArrayList list =
              (ArrayList) XposedHelpers.getObjectField(param.args[0], "emojiLikesList");
          if (list == null) return;
*/
          //  XposedHelpers.setLongField(list.get(0),"likesCnt",20L);
        });
    de.robv.android.xposed.XposedBridge.hookAllConstructors(
        ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.MsgEmojiLikes"),
        new XC_MethodHook() {
          @Override
          protected void beforeHookedMethod(MethodHookParam param) {
            param.args[2]=90L;
          param.args[3]=false;
          }
        });
  }
}

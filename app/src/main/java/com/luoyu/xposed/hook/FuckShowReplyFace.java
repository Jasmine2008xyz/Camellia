package com.luoyu.xposed.hook;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import de.robv.android.xposed.XposedHelpers;
@Deprecated
//@Xposed_Item_Controller(itemTag = "屏蔽回复表情展示")
public class FuckShowReplyFace {
    @Xposed_Item_Entry
    public void init(){
      XposedBridge.hookMethod_Before(XposedHelpers.findMethodBestMatch(ClassUtil.get("com.tencent.qqnt.aio.adapter.api.impl.AIOEmoReplyApiImpl"),"setEmojiLikes",ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.MsgRecord"),String.class,int.class,boolean.class,ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.ISetMsgEmojiLikesCallback")),param->{
        param.setResult(null);
      });
    }
}

package com.luoyu.xposed.hook.api;

import com.luoyu.utils.ClassUtil;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.data.table.SessionTable;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Xposed_Item_Controller(itemTag = "OnEntryContact", isApi = true)
public class OnEntryContact {
  /*
   * @author 小明
   */
  @Xposed_Item_Entry
  public static void start() {
    Class<?> AIOContextImpl = ClassUtil.load("com.tencent.aio.runtime.AIOContextImpl");
    XposedHelpers.findAndHookConstructor(
        AIOContextImpl,
        ClassUtil.load("com.tencent.aio.main.fragment.ChatFragment"),
        ClassUtil.load("com.tencent.aio.data.AIOParam"),
        ClassUtil.load("androidx.lifecycle.LifecycleOwner"),
        ClassUtil.load("kotlin.jvm.functions.Function0"),
        new XC_MethodHook() {
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            super.beforeHookedMethod(param);
        
            Object aIOParam = param.args[1];
            Object aIOSession =
                XposedHelpers.findFirstFieldByExactType(
                        aIOParam.getClass(), ClassUtil.get("com.tencent.aio.data.AIOSession"))
                    .get(aIOParam);

            Object aIOContact =
                XposedHelpers.findFirstFieldByExactType(
                        aIOSession.getClass(), ClassUtil.get("com.tencent.aio.data.AIOContact"))
                    .get(aIOSession);
            Object obj = aIOContact;
            // AIOContact(chatType=2, peerUid='459292855', guildId='', nick='刺痛官方:聊天区')
            Pattern pattern =
                Pattern.compile(
                    "AIOContact\\(chatType=(\\d+), peerUid='(\\d+)', guildId='(.*?)', nick='([^']*)'\\)");
            Matcher matcher = pattern.matcher(obj.toString());
            if (matcher.find()) {
              int chatType = Integer.parseInt(matcher.group(1));
              String peerUid = matcher.group(2);
              String guildId = matcher.group(3);
              String nick = matcher.group(4);
              SessionTable.ChatType = chatType;
              SessionTable.GuildId = guildId;
              SessionTable.PeerUid = peerUid;
              SessionTable.NickName = nick;
            }
          }
        });
  }
}

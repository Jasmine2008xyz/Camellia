package com.luoyu.xposed.hook.api;

import android.os.Bundle;
import com.luoyu.dexfinder.IDexFinder;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.Reflex;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder;
import com.luoyu.xposed.core.HookInstaller;
import com.luoyu.xposed.data.table.ContactTable;
import com.luoyu.xposed.data.table.SessionTable;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Xposed_Item_Controller(itemTag = "OnEntryContact", isApi = true)
public class OnEntryContact {
  public static final String TAG = "OnEntryContact";

  @Xposed_Item_Finder
  public void finder(IDexFinder finder) {
    finder.findMethodsByPathAndUseString(
        new String[] {TAG}, new String[] {"com"}, new String[] {"rootVMBuild"});
  }

  @Xposed_Item_Entry
  public void loadHook() {
    Method method = HookInstaller.Method_Map.get(TAG);
    XposedBridge.hookMethod(
        method,
        new XC_MethodHook() {
          @Override
          protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            Bundle bundle = (Bundle) param.args[0];
            Object cAIOParam = bundle.getParcelable("aio_param");

            Object AIOSession =
                Reflex.findField(cAIOParam.getClass())
                    .setReturnType("com.tencent.aio.data.AIOSession").get().get(cAIOParam);
            Object AIOContact =
                Reflex.findField(AIOSession.getClass())
                    .setReturnType("com.tencent.aio.data.AIOContact").get().get(AIOSession);
            ContactTable.chatType = XposedHelpers.getIntField(AIOContact, "e");
            ContactTable.peerUid = (String) XposedHelpers.getObjectField(AIOContact, "f");
          }
        });
  }

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

package com.luoyu.xposed.hook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import com.luoyu.dexfinder.IDexFinder;
import com.luoyu.utils.AppUtil;
import com.luoyu.utils.ConstructorUtil;
import com.luoyu.utils.Reflex;
import static com.luoyu.utils.Reflex.loadClass;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.base.QQApi;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder;
import com.luoyu.xposed.core.HookInstaller;
import com.luoyu.xposed.data.table.SessionTable;
import com.luoyu.xposed.message.MsgUtil;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Method;
import de.robv.android.xposed.XC_MethodHook;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Xposed_Item_Controller(itemTag = "聊天头像助手")
public class ChatAvatarHelper {
  public static final String TAG = "AvatarHelper";

  @Xposed_Item_Finder
  public void RFinder(IDexFinder finder) {

    finder.findMethodsByPathAndUseString(
        new String[] {TAG + "Click"},
        new String[] {"com.tencent.mobileqq.aio.msglist.holder.component.avatar"},
        new String[] {"AvatarClickIntent(msgItem="});

    finder.findMethodsByPathAndUseString(
        new String[] {TAG + "LongClick"},
        new String[] {"com.tencent.mobileqq.aio.msglist.holder.component.avatar"},
        new String[] {"AIOBubbleAvatarLongClickIntent(msgItem="});
  }

  @Xposed_Item_Entry
  public void RStart() {

    Method m =
        /*RMethodMatcher.Create(
            "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
        .ReturnType("com.tencent.mobileqq.aio.widget.AvatarContainer")
        .ParamsLength(0)
        .get();*/
        Reflex.findMethod(
                loadClass(
                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
            .setReturnType("com.tencent.mobileqq.aio.widget.AvatarContainer")
            .setParamsLength(0)
            .get();
    XposedBridge.hookMethod_After(
        m,
        param -> {
          ((RelativeLayout) param.getResult())
              .setOnClickListener(
                  v -> {
                    try {
                      Object MsgRecord =
                          XposedHelpers.callMethod(
                              /*  RFieldMatcher.CreateByObject(param.thisObject).ReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem").getContent(param.thisObject)*/
                              XposedHelpers.findFirstFieldByExactType(
                                      param.thisObject.getClass(),
                                      Reflex.loadClass("com.tencent.mobileqq.aio.msg.AIOMsgItem"))
                                  .get(param.thisObject),
                              "getMsgRecord",
                              new Object[] {});
                      int chatType = (int) XposedHelpers.getObjectField(MsgRecord, "chatType");
                      // FieldUtils.getUnknownTypeField(MsgRecord, "chatType");
                      if (chatType == 1) StartFriendDialog(MsgRecord, param.thisObject);
                      else if (chatType == 2) StartTroopDialog(MsgRecord, param.thisObject);
                    } catch (Exception err) {
                    }
                  });
        });
  }

  public void StartFriendDialog(Object MsgRecord, Object component) {
    int version = AppUtil.getVersionNameInt(HookEnv.getActivity());
    CharSequence[] items = {
      "打开主页", "复读", "@Ta", /*"一笔画红包", "一键20赞"*/
    };
    AlertDialog dialog =
        new AlertDialog.Builder(
                HookEnv.getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert)
            .setTitle("Camellia")
            .setItems(
                items,
                (dia, which) -> {
                  if (which == 0) {
                    if (version >= 9060) {
                      try {
                        Object aiomsgitem =
                            /*RMethodMatcher.Create(
                                "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                            .ReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                            .Params(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .ParamsLength(1)
                            .Callstatic(component);*/
                            Reflex.findMethod(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .setReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                                .setParams(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .get()
                                .invoke(null, component);
                        Class<?> clazz =
                            HookInstaller.Method_Map.get(TAG + "Click").getDeclaringClass();
                        Object b =
                            ConstructorUtil.callConstrutor(
                                clazz,
                                new Class[] {loadClass("com.tencent.mobileqq.aio.msg.AIOMsgItem")},
                                aiomsgitem);

                        /*RMethodMatcher.Create(
                            "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                        .Name("sendIntent")
                        .Call(component, b);*/
                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setMethodName("sendIntent")
                            .get()
                            .invoke(component, b);
                      } catch (Exception err) {
                        // QLog.e(TAG, Log.getStackTraceString(err));
                      }
                    } else {
                      /*  RMethodMatcher.Create(
                            "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                        .ReturnType(void.class)
                        .ParamsLength(2)
                        .Params(
                            ClassUtils.load(
                                "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                            View.class)
                        .Callstatic(component, new View(HostEnv.GetActivity()));
                      */
                      try {

                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setReturnType(void.class)
                            .setParams(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                                View.class)
                            .get()
                            .invoke(null, component, new View(HookEnv.getActivity()));
                      } catch (Exception err) {

                      }
                    }
                  } else if (which == 1) {
                    try {
                      ArrayList msgelements =
                          (ArrayList) XposedHelpers.getObjectField(MsgRecord, "elements");
                      // FieldUtils.getUnknownTypeField(MsgRecord, "elements");
                      String PeerUid = (String) XposedHelpers.getObjectField(MsgRecord, "peerUid");
                      // FieldUtils.getUnknownTypeField(MsgRecord, "peerUid");
                      MsgUtil.sendMsg(QQApi.createContact(1, PeerUid), msgelements);
                    } catch (Exception err) {
                      // QLog.log(TAG, Log.getStackTraceString(err));
                    }
                  } else if (which == 2) {
                    if (version >= 9060) {
                      try {
                        Object aiomsgitem =
                            Reflex.findMethod(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .setReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                                .setParams(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .get()
                                .invoke(null, component);
                        Class<?> clazz =
                            HookInstaller.Method_Map.get(TAG + "LongClick").getDeclaringClass();
                        Object b =
                            ConstructorUtil.callConstrutor(
                                clazz,
                                new Class[] {
                                  Reflex.loadClass("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                                },
                                aiomsgitem);
                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setMethodName("sendIntent")
                            .get()
                            .invoke(component, b);
                        /*    RMethodMatcher.Create(
                            "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                        .Name("sendIntent")
                        .Call(component, b);*/
                      } catch (Exception err) {

                      }
                    } else {
                      /* RMethodMatcher.Create(
                          "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                      .ReturnType(boolean.class)
                      .ParamsLength(2)
                      .Params(
                          ClassUtils.load(
                              "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                          View.class)
                      .Callstatic(component, new View(HookEnv.getActivity()));*/
                      try {

                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setReturnType(boolean.class)
                            .setParams(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                                View.class)
                            .get()
                            .invoke(null, component, new View(HookEnv.getActivity()));
                      } catch (Exception err) {

                      }
                    }
                  } /*else if (which == 3) {
                      try {
                        QQApi.ybh(
                            2,
                            HostEnv.GetContext(),
                            FieldUtils.getUnknownTypeField(MsgRecord, "peerUid"));
                      } catch (Exception err) {
                      }
                    } else if (which == 4) {
                      try {
                        QQApi.SendLike(
                            FieldUtils.getUnknownTypeField(MsgRecord, "senderUin").toString(), 20);
                      } catch (Exception err) {
                      }
                    }*/
                })
            .setPositiveButton("取消", null)
            .show();
  }

  public void StartTroopDialog(Object MsgRecord, Object component) {
    int version = AppUtil.getVersionNameInt(HookEnv.getActivity());
    CharSequence[] items = {
      "打开主页", "复读", "@Ta", /*"一笔画红包", "踢出", "一键20赞", "拍一拍", "群文件"*/
    };
    AlertDialog dialog =
        new AlertDialog.Builder(
                HookEnv.getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert)
            .setTitle("Camellia")
            .setItems(
                items,
                (dia, which) -> {
                  if (which == 0) {
                    if (version >= 9060) {
                      try {
                        Object aiomsgitem =
                            /*RMethodMatcher.Create(
                                "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                            .ReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                            .Params(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .ParamsLength(1)
                            .Callstatic(component);*/
                            Reflex.findMethod(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .setReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                                .setParams(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .get()
                                .invoke(null, component);
                        Class<?> clazz =
                            HookInstaller.Method_Map.get(TAG + "Click").getDeclaringClass();
                        Object b =
                            ConstructorUtil.callConstrutor(
                                clazz,
                                new Class[] {loadClass("com.tencent.mobileqq.aio.msg.AIOMsgItem")},
                                aiomsgitem);

                        /*RMethodMatcher.Create(
                            "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                        .Name("sendIntent")
                        .Call(component, b);*/
                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setMethodName("sendIntent")
                            .get()
                            .invoke(component, b);
                      } catch (Exception err) {
                      }
                    } else {
                      try {

                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setReturnType(void.class)
                            .setParams(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                                View.class)
                            .get()
                            .invoke(null, component, new View(HookEnv.getActivity()));
                      } catch (Exception err) {

                      }
                    }
                  } else if (which == 1) {
                    try {
                      ArrayList msgelements =
                          (ArrayList) XposedHelpers.getObjectField(MsgRecord, "elements");
                      // FieldUtils.getUnknownTypeField(MsgRecord, "elements");
                      String PeerUid = (String) XposedHelpers.getObjectField(MsgRecord, "peerUid");
                      // FieldUtils.getUnknownTypeField(MsgRecord, "peerUid");
                      MsgUtil.sendMsg(QQApi.createContact(2, PeerUid), msgelements);
                    } catch (Exception err) {
                      // QLog.log(TAG, Log.getStackTraceString(err));
                    }
                  } else if (which == 2) {
                    if (version >= 9060) {
                      try {
                        Object aiomsgitem =
                            Reflex.findMethod(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .setReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                                .setParams(
                                    loadClass(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .get()
                                .invoke(null, component);
                        Class<?> clazz =
                            HookInstaller.Method_Map.get(TAG + "LongClick").getDeclaringClass();
                        Object b =
                            ConstructorUtil.callConstrutor(
                                clazz,
                                new Class[] {
                                  Reflex.loadClass("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                                },
                                aiomsgitem);
                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setMethodName("sendIntent")
                            .get()
                            .invoke(component, b);
                        /*    RMethodMatcher.Create(
                            "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                        .Name("sendIntent")
                        .Call(component, b);*/
                      } catch (Exception err) {

                      }
                    } else {
                      /* RMethodMatcher.Create(
                          "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                      .ReturnType(boolean.class)
                      .ParamsLength(2)
                      .Params(
                          ClassUtils.load(
                              "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                          View.class)
                      .Callstatic(component, new View(HookEnv.getActivity()));*/
                      try {

                        Reflex.findMethod(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                            .setReturnType(boolean.class)
                            .setParams(
                                loadClass(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                                View.class)
                            .get()
                            .invoke(null, component, new View(HookEnv.getActivity()));
                      } catch (Exception err) {

                      }
                    }
                  } /*else if (which == 2) {
                      if (version >= 9060) {
                        Object aiomsgitem =
                            RMethodMatcher.Create(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                                .ReturnType("com.tencent.mobileqq.aio.msg.AIOMsgItem")
                                .Params(
                                    ClassUtils.load(
                                        "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"))
                                .ParamsLength(1)
                                .Callstatic(component);
                        Class<?> clazz =
                            HookInstaller.Method_Map.get(TAG + "LongClick").getDeclaringClass();
                        Object b =
                            ConstructorUtil.callConstrutor(
                                clazz,
                                new Class[] {loadClass("com.tencent.mobileqq.aio.msg.AIOMsgItem")},
                                aiomsgitem);

                        RMethodMatcher.Create(
                                "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                            .Name("sendIntent")
                            .Call(component, b);
                      } else {
                        RMethodMatcher.Create(
                                "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent")
                            .ReturnType(boolean.class)
                            .ParamsLength(2)
                            .Params(
                                ClassUtils.load(
                                    "com.tencent.mobileqq.aio.msglist.holder.component.avatar.AIOAvatarContentComponent"),
                                View.class)
                            .Callstatic(component, new View(HostEnv.GetActivity()));
                      }
                    } else if (which == 3) {
                      try {
                        QQApi.ybh(
                            3,
                            HostEnv.GetContext(),
                            FieldUtils.getUnknownTypeField(MsgRecord, "peerUid"));
                      } catch (Exception err) {
                      }
                    } else if (which == 4) {
                      try {
                        TroopApi.Group_Kick(
                            (String) FieldUtils.getUnknownTypeField(MsgRecord, "peerUid"),
                            FieldUtils.getUnknownTypeField(MsgRecord, "senderUin").toString(),
                            false);
                      } catch (Exception err) {
                      }
                    } else if (which == 5) {
                      try {
                        QQApi.SendLike(
                            FieldUtils.getUnknownTypeField(MsgRecord, "senderUin").toString(), 20);
                      } catch (Exception err) {

                      }
                    } else if (which == 6) {
                      try {
                        QQApi.sendPaiyiPai(
                            FieldUtils.getUnknownTypeField(MsgRecord, "peerUid"),
                            FieldUtils.getUnknownTypeField(MsgRecord, "senderUin").toString(),
                            2,
                            100);
                      } catch (Exception err) {
                        QLog.log(TAG, Log.getStackTraceString(err));
                      }
                    } else if (which == 7) {
                      /*   ArrayList l=new ArrayList();
                      FileElement f=new FileElement();
                      f.expireTime=0L;
                      f.fileBizId=102;
                      f.fileMd5="123456";
                      f.fileName="你就下吧谁能下的过你啊！";
                      f.transferStatus=4;
                      f.fileSize=9999999999999L;
                      f.subElementType=9;
                      f.fileUuid="/1122";
                      f.picWidth=1024;
                      f.picHeight=1024;
                      try {
                          Object MsgElement=ClassUtils.get("com.tencent.qqnt.kernel.nativeinterface.MsgElement").newInstance();
                          RFieldMatcher.CreateByObject(MsgElement).Name("fileElement").get().set(MsgElement,f);
                      l.add(
                          MsgElement
                          );
                      MsgUtils.send(QQApi.CreateContact(2,FieldUtils.getUnknownTypeField(MsgRecord, "peerUid")),l);

                      try {
                        /*   new MaterialAlertDialogBuilder(HostEnv.GetActivity(), 0)
                        .setTitle("测试")
                        .show();
                      } catch (Exception err) {
                        // QLog.log(TAG, Log.getStackTraceString(err));
                      }
                    }*/
                })
            .setPositiveButton("取消", null)
            .show();
  }
}

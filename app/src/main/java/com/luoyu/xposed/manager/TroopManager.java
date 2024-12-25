package com.luoyu.xposed.manager;

import android.util.Log;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.Reflex;
import com.luoyu.xposed.base.HookEnv;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Method;
import java.util.ArrayList;
import com.luoyu.xposed.base.QRoute;
import java.util.concurrent.CopyOnWriteArrayList;

public class TroopManager {
  public static void Group_Forbidden_AllFalse(String Group, boolean isban) throws Exception {
    if (Group.contains("&")) {
      String[] GuildCut = Group.split("&");

      // QQGuild_Manager.Guild_ForbiddenAll(GuildCut[0],isban ? 3600 * 24 * 30 : 0);
      return;
    }
    /*   Object TroopGagManager =/* MethodUtils.CallMethod(HookEnv.AppInterface,HookEnv.AppInterface.getClass(),"getBusinessHandler",
    XposedHelpers.findClass("com.tencent.mobileqq.app.BusinessHandler",HookEnv.mLoader),
    new Class[]{String.class},
    new Object[]{FieldTool.GetField(null,ClassTool.loadClass("com.tencent.mobileqq.app.BusinessHandlerFactory"),"TROOP_GAG_HANDLER",1)});*/
    /*     XposedHelpers.callMethod(HookEnv.getAppInterFace(),"getBusinessHandler",)
    MethodUtils.CallMethodNoName(TroopGagManager,void.class,new Class[]{String.class,long.class},new Object[]{
            Group,1L
    });*/
  }

  public static void checkSign(String GroupUin, String UserUin) {
    try {
      Object TroopHandle =
          XposedHelpers.callMethod(
              HookEnv.getAppInterFace(),
              "getBusinessHandler",
              XposedHelpers.findField(
                      Reflex.loadClass("com.tencent.mobileqq.app.BusinessHandlerFactory"),
                      "TROOP_CLOCKIN_HANDLER")
                  .get(null));

      Method med =
          Reflex.findMethod(TroopHandle.getClass())
              .setReturnType(void.class)
              .setParams(String.class, String.class, int.class, boolean.class)
              .get();

      med.setAccessible(true);
      med.invoke(TroopHandle, GroupUin, UserUin, 0, true);
    } catch (Exception e) {
      //  QLog.log("Group_CheckSign",Log.getStackTraceString(e));
      // ForLogUtils.Error("打卡失败",e);
    }
  }

  public static class GroupInfo {
    public String Creator;
    public ArrayList<String> adminList;
    public String Name;
    public String Uin;
    public Object source;
    public String Code;
  }

  public static final String TAG = "GroupUtils";

  public static ArrayList<GroupInfo> Group_Get_List() {
    try {
      /*Object TroopInfoServer = QQManager.getRuntimeService(ClassUtil.load("com.tencent.mobileqq.troop.api.ITroopInfoService"));
                  ArrayList<?> rawList =(ArrayList)XposedHelpers.callMethod(TroopInfoServer,"getUiTroopListWithoutBlockedTroop");
                  // MethodTool2.CallMethodNoParam(TroopInfoServer, "getUiTroopListWithoutBlockedTroop", ArrayList.class);
      */ Object inst =
          QRoute.api(Reflex.loadClass("com.tencent.qqnt.troop.ITroopListRepoApi"));
      CopyOnWriteArrayList<?> rawList = (CopyOnWriteArrayList) XposedHelpers.callMethod(inst, "getTroopListFromCache");
      ArrayList<GroupInfo> NewList = new ArrayList<>();
      for (Object item : rawList) {
        GroupInfo NewItem = new GroupInfo();
        NewItem.Uin = (String) XposedHelpers.getObjectField(item, "troopuin");
        // FieldUtils.getUnknownTypeField(item, "troopuin");
        NewItem.Name = (String) XposedHelpers.getObjectField(item, "troopname");
        /*NewItem.Creator = FieldUtils.getUnknownTypeField(item, "troopowneruin");
        String admins = FieldUtils.getUnknownTypeField(item, "Administrator");
        if (admins == null) {
            NewItem.adminList = new ArrayList<>();
        } else {
            NewItem.adminList = new ArrayList<>(Arrays.asList(admins.split("\\|")));
        }*/
        NewList.add(NewItem);
      }
      return NewList;
    } catch (Exception e) {
            XposedBridge.log("获取群聊列表异常"+Log.getStackTraceString(e));
      //    ForLogUtils.Error("Group_Get_List", e);
      return null;
    }
  }
}

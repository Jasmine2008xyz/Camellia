package com.luoyu.xposed.manager;
import com.luoyu.xposed.base.HookEnv;
import de.robv.android.xposed.XposedHelpers;

public class TroopManager {
    public static void Group_Forbidden_AllFalse(String Group,boolean isban) throws Exception {
        if(Group.contains("&")){
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
}

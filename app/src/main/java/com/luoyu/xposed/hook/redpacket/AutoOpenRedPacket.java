package com.luoyu.xposed.hook.redpacket;
import com.luoyu.dexfinder.IDexFinder;
import com.luoyu.utils.XposedBridge;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.annotations.Xposed_Item_Finder;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick;
import com.luoyu.xposed.core.HookInstaller;
import com.luoyu.xposed.message.MsgUtil;
import com.luoyu.xposed.base.QQApi;
import com.luoyu.utils.ClassUtil;
import de.robv.android.xposed.XposedHelpers;
import java.lang.reflect.Method;

import java.lang.reflect.Field;
import java.util.ArrayList;
@Xposed_Item_Controller(itemTag = "自动抢红包")
public class AutoOpenRedPacket {
    @Xposed_Item_UiLongClick
    public void onLongClick() {
        /*OpenRedPacket.OpenLuckyRedPack("ac202b77be50886b35b5286237187dcb8w","1","10000452012409163700114624310700",OpenRedPacket.getSkey(),1,"240214519","2968447202","[QQ红包]大吉大利");*/
        Object contact = createContact(2, "646945623");
        ArrayList list = new ArrayList();
        try {
        Object msgElement = ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.MsgElement").newInstance();
       XposedHelpers.setIntField(msgElement, "elementType", 18);
       Object textgift = ClassUtil.get("com.tencent.qqnt.kernelpublic.nativeinterface.TextGiftElement").newInstance();
       XposedHelpers.setLongField(textgift, "giftId", 300434L);
       XposedHelpers.setObjectField(textgift, "giftName", "一个大逼斗");
       XposedHelpers.setLongField(textgift, "receiverUin", 7278121L);
       XposedHelpers.setLongField(textgift, "senderUin", 2968447202L);
       XposedHelpers.setObjectField(textgift, "receiverNick", "小星");
       XposedHelpers.setObjectField(textgift, "senderNick", "小明");
       XposedHelpers.setLongField(textgift, "price", 10086L);
       XposedHelpers.setObjectField(textgift, "paddingTop", "  a");
       XposedHelpers.setLongField(textgift, "tianquanId", 9221120237041091000L);
       XposedHelpers.setIntField(textgift, "level", 1);
       XposedHelpers.callMethod(msgElement, "setTextGiftElement", textgift);
       list.add(msgElement);
        MsgUtil.sendMsg(contact, list);
        }catch(Exception e){ 
        }
    }
    @Xposed_Item_Finder
    public void find(IDexFinder finder) {
        finder.findMethodsByPathAndUseString(new String[]{"AutoOpenRedPacket_1"},new String[]{"com.tencent.qqnt.msg"},new String[]{"[不支持的元素类型]", "[图片]", "[文件]", "[emoji]"});
    }
    @Xposed_Item_Entry
    public void hook(){
        Method m = HookInstaller.Method_Map.get("AutoOpenRedPacket_1");
        XposedBridge.hookMethod_After(XposedHelpers.findMethodsByExactParameters(m.getDeclaringClass(),ArrayList.class,ArrayList.class)[0],param->{
            Object msgRecord = ((ArrayList)param.args[0]).get(0);
                String peerUid = (String) XposedHelpers.getObjectField(msgRecord,"peerUid");
                String senderUin = Long.toString(XposedHelpers.getLongField(msgRecord,"senderUin"));
                ArrayList elements = (ArrayList) XposedHelpers.getObjectField(msgRecord,"elements");
                for(Object msgElement : elements) {
                	if(XposedHelpers.getIntField(msgElement,"elementType")==9) {
                		Object walletElement = XposedHelpers.getObjectField(msgElement,"walletElement");
                        Object wallAio = XposedHelpers.getObjectField(walletElement,"sender");
                        Object realAio = XposedHelpers.getObjectField(walletElement,"receiver");
                        String authKey = (String)XposedHelpers.getObjectField(walletElement,"authkey");
                        String notice = (String) XposedHelpers.getObjectField(realAio,"notice");
                        String billNo = (String) XposedHelpers.getObjectField(walletElement,"billNo");
                        OpenRedPacket.OpenLuckyRedPack(authKey,"1",billNo,OpenRedPacket.getSkey(),1,peerUid,senderUin,notice);
                	}
                }
        });
    }
    
    public Object createContact(int chatType, String uid) {
    Object contact;
    try {
        contact = Classes.getContactClass().newInstance();
        Class<?> contactClass = contact.getClass();
        
        // 设置 chatType
        Field chatTypeField = contactClass.getDeclaredField("chatType");
        chatTypeField.setAccessible(true);
        chatTypeField.set(contact, chatType);
        
        if (chatType == 2) {
            Field peerUidField = contactClass.getDeclaredField("peerUid");
            peerUidField.setAccessible(true);
            peerUidField.set(contact, uid);
        } else if (chatType == 4) {
            Field guildIdField = contactClass.getDeclaredField("guildId");
            guildIdField.setAccessible(true);
            guildIdField.set(contact, uid);
        } else if (uid.equals(QQUtil.getCurrentUin())) {
            Field peerUidField = contactClass.getDeclaredField("peerUid");
            peerUidField.setAccessible(true);
       //     peerUidField.set(contact, getUidFromUin(uid));
        } else {
            Field peerUidField = contactClass.getDeclaredField("peerUid");
            peerUidField.setAccessible(true);
            peerUidField.set(contact, uid);
        }
        return contact;
    } catch (Exception ex) {
      //  LogCat.e("createContact错误", Util.getStackTraceString(ex));
        throw new RuntimeException("create failed, since:" + ex);
    }
}
}

package com.luoyu.xposed.hook.redpacket;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_UiLongClick;
@Xposed_Item_Controller(itemTag = "自动抢红包")
public class AutoOpenRedPacket {
    @Xposed_Item_UiLongClick
    public void onLongClick() {
        OpenRedPacket.OpenLuckyRedPack("ac202b77be50886b35b5286237187dcb8w","1","10000452012409163700114624310700",OpenRedPacket.getSkey(),1,"240214519","2968447202","大吉大利");
    }
}

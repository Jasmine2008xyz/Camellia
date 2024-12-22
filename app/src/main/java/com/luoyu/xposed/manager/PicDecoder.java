package com.luoyu.xposed.manager;
import de.robv.android.xposed.XposedHelpers;

public class PicDecoder {
/*  public static String getPicElementUrl(int chatType, Object picElement) {
        String str = (String)XposedHelpers.getObjectField(picElement,"originImageUrl");
    
        String str2 = (String) XposedHelpers.getObjectField(picElement,"fileUuid");
        String str3 = (String) XposedHelpers.getObjectField(picElement,"md5HexStr");
        if (str != null && !str.isEmpty()) {
            if (!str.startsWith("/download")) {
                return "https://gchat.qpic.cn" + str;
            }
            String rkeyPrivate = OnQQRKeyApi.getRkeyPrivate();
            if (str.contains("appid=1406")) {
                rkeyPrivate = OnQQRKeyApi.getRkeyGroup();
            }
            return "https://gchat.qpic.cn" + str + rkeyPrivate;
        }
        if (str2.length() < 64) {
            return "https://gchat.qpic.cn/gchatpic_new/0/0-0-" + str3.toUpperCase() + "/0?term=2&is_origin=1";
        }
        String str4 = chatType == 1 ? "1406" : chatType == 2 ? "1407" : "";
        String rkeyPrivate2 = OnQQRKeyApi.getRkeyPrivate();
        if (str4.equals("1406")) {
            rkeyPrivate2 = OnQQRKeyApi.getRkeyGroup();
        }
        return "https://gchat.qpic.cn/download?appid=" + str4 + "&fileid=" + str2 + "&spec=0" + rkeyPrivate2;
    }*/
}

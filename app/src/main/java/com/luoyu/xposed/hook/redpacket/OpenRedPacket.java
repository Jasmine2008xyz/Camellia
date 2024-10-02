package com.luoyu.xposed.hook.redpacket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.ConstructorUtil;
import com.luoyu.utils.DataUtil;
import com.luoyu.utils.temp.MField;
import com.luoyu.utils.temp.MMethod;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.logging.LogCat;
import com.luoyu.xposed.manager.QQKeys;
import com.luoyu.xposed.utils.QQUtil;
import com.luoyu.xposed.base.QRoute;
import de.robv.android.xposed.XposedHelpers;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import org.json.JSONObject;

public class OpenRedPacket {
  private static final String grap_hb_url =
      "https://mqq.tenpay.com/cgi-bin/hongbao/qpay_hb_na_grap.cgi?ver=2.0&chv=3";
  private static final String hb_pre_grap =
      "https://mqq.tenpay.com/cgi-bin/hongbao/hb_pre_grap.cgi?ver=2.0&chv=3";
  static int KeyIndex = -1;
  public static String str5 = "";

  public static void OpenLuckyRedPack(
      String AuthKey,
      String Channel,
      String listid,
      String skey,
      int grouptype,
      String groupuin,
      String SenderUin,
      String Desc) {
    InitKeyIndex();
    try {
      StringBuilder postData = new StringBuilder();
      postData
          .append("listid=")
          .append(listid)
          .append("&authkey=")
          .append(AuthKey)
          .append("&hb_from=0")
          .append("&grouptype=")
          .append(grouptype)
          .append("&trans_seq=")
          .append(KeyIndex)
          .append("&groupuin=")
          .append(groupuin)
          .append("&pay_flag=")
          .append(0)
          .append("&groupid=")
          .append(groupuin)
          .append("&channel=")
          .append(Channel)
          .append("&name=")
          .append(URLEncoder.encode("小明", "UTF-8"))
          .append("&uin=")
          .append(QQUtil.getCurrentUin())
          .append("&senderuin=")
          .append(SenderUin);
      // true
      String data = EncText(postData.toString(), "hb_pre_grapver=2.0&chv=3");
      //   LogCat.d("加密",data);
      Intent NewIntent =
          ConstructorUtil.callConstrutor(
              ClassUtil.load("mqq.app.NewIntent"),
              new Class[] {Context.class, Class.class},
              MField.GetField(null, ClassUtil.load("mqq.app.MobileQQ"), "sMobileQQ", 1),
              ClassUtil.load(
                  /*"com.tencent.mobileqq.qwallet.servlet.GdtAdServlet"*/ "com.tencent.mobileqq.qwallet.e.b"));
      NewIntent.putExtra("cmd", "trpc.qqhb.qqhb_proxy.Handler.sso_handle");

      Object QQHBRequest =
          ConstructorUtil.callConstrutor(
              ClassUtil.load("tencent.im.qqwallet.QWalletHbPreGrab$QQHBRequest"), new Class[0]);
      MMethod.CallMethod(
          MField.GetField(QQHBRequest, "cgiName"),
          "set",
          void.class,
          new Class[] {String.class},
          "hb_pre_grap");
      MMethod.CallMethod(
          MField.GetField(QQHBRequest, "reqText"),
          "set",
          void.class,
          new Class[] {String.class},
          data);
      MMethod.CallMethod(
          MField.GetField(QQHBRequest, "random"),
          "set",
          void.class,
          new Class[] {String.class},
          /* Integer.toString(KeyIndex)*/ KeyIndex + "");
      /*    MMethod.CallMethod(
      MField.GetField(QQHBRequest, "reqBody"), "set", void.class, new Class[] {ClassUtil.get("com.tencent.mobileqq.pb.ByteStringMicro")}, new Object[]{ConstructorUtil.callConstrutor(ClassUtil.get("com.tencent.mobileqq.pb.ByteStringMicro"),new Class[]{byte[].class},new byte[]{1})});
        */
      MMethod.CallMethod(
          MField.GetField(QQHBRequest, "enType"),
          "set",
          void.class,
          new Class[] {int.class},
          new Object[] {0});

      NewIntent.putExtra(
          "data", a(MMethod.CallMethod(QQHBRequest, "toByteArray", byte[].class, new Class[0])));
      // LogCat.d("data", a(MMethod.CallMethod(QQHBRequest, "toByteArray", byte[].class, new
      // Class[0])));
      MMethod.CallMethod(
          NewIntent,
          "setObserver",
          void.class,
          new Class[] {ClassUtil.load("mqq.observer.BusinessObserver")},
          Proxy.newProxyInstance(
              HookEnv.getHostClassLoader(),
              new Class[] {ClassUtil.load("mqq.observer.BusinessObserver")},
              (proxy, method, args) -> {
                Bundle bundle = (Bundle) args[2];
                //            LogCat.d("bundle",bundle.toString());
                byte[] dataaaa = bundle.getByteArray(/*"rsp_bytes"*/ "data");
                //   LogCat.d("data",dataaaa);
                Object HBReply =
                    ConstructorUtil.callConstrutor(
                        ClassUtil.load("tencent.im.qqwallet.QWalletHbPreGrab$QQHBReply"),
                        new Class[0]);
                MMethod.CallMethod(
                    HBReply,
                    ClassUtil.load("com.tencent.mobileqq.pb.MessageMicro"),
                    "mergeFrom",
                    new Class[] {byte[].class},
                    dataaaa);
                String Result =
                    MMethod.CallMethod(
                        MField.GetField(HBReply, "rspText"), "get", String.class, new Class[0]);
                Result = DecText(Result, "hb_pre_grap");
                JSONObject grapJSON = new JSONObject(Result);
                // MLogCat.Print_Debug(Result);
                LogCat.d("调试grapJSON", grapJSON.toString());
                if (grapJSON.optString("retcode").equals("0") && grapJSON.has("pre_grap_token")) {
                  String preCode = grapJSON.getString("pre_grap_token");

                  StringBuilder postd = new StringBuilder();
                  postd
                      .append("authkey=")
                      .append(AuthKey)
                      .append("&hb_from=0")
                      .append("&groupid=")
                      //   .append("&groupuin=")
                      .append(groupuin)
                      .append("&agreement=")
                      .append(0)
                      .append("&pay_flag=")
                      .append(0)
                      .append("&channel=")
                      .append(1)
                      .append("&pre_grap_token=")
                      .append(URLEncoder.encode(preCode, "UTF-8"))
                      .append("&senderuin=")
                      .append(SenderUin)
                      .append("&listid=")
                      .append(listid)
                      .append("&skey_type=")
                      .append(skey.length() > 12 ? "0" : "2")
                      .append("&grouptype=")
                      .append(grouptype)
                      .append("&groupuin=")
                      .append(groupuin)
                      .append("&name=")
                      .append(URLEncoder.encode("小明", "UTF-8"))
                      .append("&skey=")
                      .append(skey)
                      .append("&uin=")
                      .append(QQUtil.getCurrentUin());

                  String re = POSTForGrapCgi(postd.toString(), skey);
                  DecodeJson(re, groupuin, SenderUin, "[拼手气/普通]" + Desc);
                }
                return null;
              }));

      MMethod.CallMethod(
          HookEnv.getAppInterFace(),
          "startServlet",
          void.class,
          new Class[] {ClassUtil.load("mqq.app.NewIntent")},
          NewIntent);

    } catch (Throwable th) {
      LogCat.e("Grap_Lucky_RedPack", Log.getStackTraceString(th));
    }
  }

  private static String POSTForGrapCgi(String PostData, String skey) {
    try {
      if (Thread.currentThread().getName().equals("main")) {
        StringBuilder builder = new StringBuilder();
        Thread newThread =
            new Thread(
                () -> {
                  builder.append(POSTForGrapCgi(PostData, skey));
              });
        newThread.start();
        newThread.join();
        return builder.toString();
      }
      URL u = new URL(grap_hb_url);
      HttpURLConnection con = (HttpURLConnection) u.openConnection();
      con.setRequestProperty("user-agent", "okhttp/3.12.10");
      con.setDoOutput(true);
      OutputStream out = con.getOutputStream();

      String post =
          "req_text="
              + EncText(
                  PostData,
                  "https://mqq.tenpay.com/cgi-bin/hongbao/qpay_hb_na_grap.cgi?ver2.0&chv=3");
      String skeyType = "2&random=" + KeyIndex;
      if (skey.startsWith("v") && skey.length() > 12) {
        skeyType = "0";
      }
      SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
      post += "&skey_type=" + skeyType + "&msgno=" + getMsgNo(QQUtil.getCurrentUin());
      post += "&skey=" + skey;

      out.write(post.getBytes(StandardCharsets.UTF_8));
      out.flush();

      InputStream insp = con.getInputStream();
      String ret = new String(DataUtil.readAllBytes(insp));

      String decResult =
          DecText(ret, "https://mqq.tenpay.com/cgi-bin/hongbao/qpay_hb_na_grap.cgi?");
      //  LogCat.d("Result",decResult);
      return decResult;
    } catch (Exception e) {
      return null;
    }
  }

  public static void DecodeJson(String JSONData, String TroopUin, String SenderUin, String Desc) {
    try {
      JSONObject json = new JSONObject(JSONData);
      LogCat.d("json", json.toString());
      try {
        String skey = json.getString("skey");
        String skeyTime = json.getString("skey_expire");
        long endTime = System.currentTimeMillis() + Long.parseLong(skeyTime) * 1000;
        int mkeyIndex = Integer.parseInt(json.getString("trans_seq"));

        SaveSkey(skey, endTime, mkeyIndex);
      } catch (Exception e) {

      }
      if (json.optString("retmsg").equals("ok")) {

        if (json.has("recv_object")) {
          JSONObject recvData = json.getJSONObject("recv_object");
          Object Money = recvData.get("amount");
          int getMoney;
          if (Money instanceof String) {
            getMoney = Integer.parseInt((String) Money);
          } else {
            getMoney = (int) Money;
          }
          HB_Sum(getMoney, TroopUin, SenderUin, Desc);
        }
      }
    } catch (Exception e) {

    }
  }

  static int index = 1;

  public static String getMsgNo(String str) {
    String format = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    StringBuilder sb = new StringBuilder();
    sb.append(str);
    sb.append(format);
    int i = index;
    index = i + 1;
    String valueOf = String.valueOf(i);
    int length = (28 - sb.length()) - valueOf.length();
    for (int i2 = 0; i2 < length; i2++) {
      sb.append("0");
    }
    sb.append(valueOf);
    return sb.toString();
  }

  // true
  private static String DecText(String text, String url) {
    try {
      Object EncRequest =
          ConstructorUtil.callConstrutor(
              ClassUtil.load("com.tenpay.sdk.basebl.EncryptRequest"),
              new Class[] {Context.class},
              HookEnv.getContext());
      Object encResult =
          MMethod.CallMethod(
              EncRequest,
              "decypt",
              ClassUtil.load("com.tenpay.sdk.basebl.DecytBean"),
              new Class[] {String.class, String.class, int.class, String.class},
              QQUtil.getCurrentUin(),
              url,
              KeyIndex,
              text);
      return MField.GetField(encResult, "decryptStr");

    } catch (Throwable th) {
      LogCat.e("RequestEncoder", th);
      return null;
    }
  }

  // true
  private static String EncText(String text, String URL) {
    try {

      Object EncRequest =
          ConstructorUtil.callConstrutor(
              ClassUtil.load("com.tenpay.sdk.basebl.EncryptRequest"),
              new Class[] {Context.class},
              HookEnv.getContext());
      String psKey = QQKeys.getPsKey("tenpay.com");
      // LogCat.d("获取str5",""+text+"|"+URL);
      Object encResult =
          MMethod.CallMethod(
              EncRequest,
              "encypt",
              ClassUtil.load("com.tenpay.sdk.basebl.EncryptRequest$Encrypt"),
              new Class[] {
                String.class, String.class, int.class, String.class, String.class, String.class
              },
              QQUtil.getCurrentUin(),
              URL,
              KeyIndex,
              text,
              QQKeys.getPsKey("tenpay.com"),
              /*"F3CF43293EFA13ED060E89DC0F4A6712"*/(String) XposedHelpers.callMethod(QRoute.api(ClassUtil.get("com.tencent.mobileqq.qwallet.api.INewQWalletApi")), "hexGuid", new Object[0]));
      //   LogCat.d("获取对象",encResult.toString());
      return (String) XposedHelpers.getObjectField(encResult, "encText");
      // MField.GetField(encResult, "encText");

    } catch (Throwable th) {
      LogCat.e("RequestEncoder", th);
      return null;
    }
  }

  public static String getSkey() {
    /*  long time = GlobalConfig.Get_Long("SkeyTime",0);
    if (time < System.currentTimeMillis())return QQTicketUtils.GetSkey();
    return GlobalConfig.Get_String("skey");*/
    return QQKeys.getSkey();
  }

  private static int getKeyIndex() {
    int index = (int) ModuleController.Config.getData("keyIndex", -1);
    long time = (int) ModuleController.Config.getData("SkeyTime", 0);
    if (time < System.currentTimeMillis()) return new Random().nextInt(16);
    if (index > -1) return index;
    return new Random().nextInt(16);
  }

  public static void SaveSkey(String skey, long time, int KeyeIndex) {
    KeyIndex = KeyeIndex;
    ModuleController.Config.putData("keyIndex", KeyeIndex);
    ModuleController.Config.putData("SkeyTime", time);
    ModuleController.Config.putData("skey", skey);
  }

  public static void InitKeyIndex() {
    if (KeyIndex == -1) KeyIndex = getKeyIndex();
  }

  /*
  public static void NotifyMessageRecv(Object MessageObj){
      try{
          if (!Hook_RedPacket_DoParse.IsAvailable()){
              HBQueue.clear();
              return;
          }
          String SenderUin = MField.GetField(MessageObj,"senderuin",String.class);
          String clzName = MessageObj.getClass().getName();
          if (clzName.contains("MessageForText") && SenderUin.equals(BaseInfo.GetCurrentUin())){
              if (HBQueue.size() > 0){
                  Iterator<InvokerLoop> its = HBQueue.iterator();
                  while (its.hasNext()){
                      InvokerLoop loop = its.next();
                      its.remove();
                      new Handler(Looper.getMainLooper())
                              .postDelayed(()-> loop.MakeCall(MessageObj),new Random().nextInt(100)+100);

                  }
              }
          }

      }catch (Exception e){

      }
  }
  */
  static LinkedList<InvokerLoop> HBQueue = new LinkedList<>();

  interface InvokerLoop {
    void MakeCall(Object record);
  }

  private static void HB_Sum(int Count, String TroopUin, String UserUin, String DescText) {
    String ShowText =
        "抢到" + Count / 100 + "." + ((Count % 100) < 10 ? ("0" + (Count % 100)) : (Count % 100));
    ShowText = ShowText + "\n(" + DescText + ")";
    Toast.makeText(HookEnv.getContext(), ShowText, 0).show();
  }

  // com.tencent.mobileqq.utils.WupUtil.a(byte[])
  public static byte[] a(byte[] bArr) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(bArr.length + 4);
    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    try {
      try {
        dataOutputStream.writeInt(bArr.length + 4);
        dataOutputStream.write(bArr);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
          byteArrayOutputStream.close();
          dataOutputStream.close();
        } catch (Exception unused) {
        }
        return byteArray;
      } catch (Throwable th) {
        try {
          byteArrayOutputStream.close();
          dataOutputStream.close();
        } catch (Exception unused2) {
        }
        throw th;
      }
    } catch (Exception e) {
      e.printStackTrace();
      try {
        byteArrayOutputStream.close();
        dataOutputStream.close();
        return null;
      } catch (Exception unused3) {
        return null;
      }
    }
  }

  @SuppressWarnings("deprecation")
  public static String getPskeyV2(String url) {
    try {
      return (String)
          XposedHelpers.callMethod(
              ClassUtil.get("com.tenpay.sdk.net.core.processor.PsKeyProcessor").newInstance(),
              "getPsKey",
              url);
    } catch (Exception err) {
      throw new RuntimeException("获取Pskey错误:" + err);
    }
  }
}

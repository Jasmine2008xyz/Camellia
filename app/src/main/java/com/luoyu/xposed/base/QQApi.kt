package com.luoyu.xposed.base

import android.content.Context
import android.content.Intent
import android.net.Uri

import com.luoyu.camellia.utils.Classes
import com.luoyu.camellia.utils.ClassUtil
import com.luoyu.camellia.utils.Util
import com.luoyu.xposed.logging.LogCat

import com.luoyu.xposed.ModuleController

import com.luoyu.camellia.qqmanager.QQUtil

import com.luoyu.camellia.qqmanager.QRoute
        
import de.robv.android.xposed.XposedHelpers

import java.lang.reflect.Method

class QQApi {
  companion object {
    
  /** uin转peerUid */
  fun getUidFromUin(uin: String): String {
    val obj: Any? = QRoute.api(ClassUtil.get("com.tencent.relation.common.api.IRelationNTUinAndUidApi"))
    return XposedHelpers.callMethod(obj, "getUidFromUin", uin) as String
  }

  /** peerUid转uin */
  fun getUinFromUid(uid: String): String {
    val obj: Any? = QRoute.api(ClassUtil.get("com.tencent.relation.common.api.IRelationNTUinAndUidApi"))
    return XposedHelpers.callMethod(obj, "getUinFromUid", uid) as String
  }
  
  /*
   * 祖传代码
   */
  
  fun openGroup(context: Context,groupUin: String) {
    val u: Uri =
        Uri.parse(
            "mqq://card/show_pslcard?src_type=internal&version=1&uin=${groupUin}&card_type=group&source=qrcode")
    val i: Intent = Intent(Intent.ACTION_VIEW, u)
    i.setPackage("com.tencent.mobileqq")
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(i)
  }

  fun openQQ(context: Context , qq: String) {
    val u: Uri =
        Uri.parse(
            "mqq://card/show_pslcard?src_type=internal&version=1&uin=${qq}&card_type=friend&source=qrcode");
    val i: Intent = Intent(Intent.ACTION_VIEW, u)
    i.setPackage("com.tencent.mobileqq")
    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(i)
  }
  
  fun findCommonGroup(context: Context, uin: String) {
    try {
      val browser: Class<*> = ClassUtil.load("com.tencent.mobileqq.activity.QQBrowserDelegationActivity")
      val intent: Intent = Intent(context, browser);
      intent.putExtra("fling_action_key", 2);
      intent.putExtra("fling_code_key", context.hashCode());
      intent.putExtra("useDefBackText", true);
      intent.putExtra("param_force_internal_browser", true);
      intent.putExtra("url", "https://ti.qq.com/friends/recall?uin=" + uin);
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    } catch (ex :Exception) {}
  }
  
  fun openUrl(context: Context, url: String) {
    val uri: Uri = Uri.parse(url);
    val intent: Intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.tencent.mobileqq")
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
  }
  
  fun createContact(chatType: Int, uid: String): Any? {
    val contact = Classes.getContactClass().newInstance()
    try{
        contact::class.java.getDeclaredField("chatType").set(contact,chatType)
        if(chatType == 2) contact::class.java.getDeclaredField("peerUid").set(contact,uid)
        else if (chatType == 4) contact::class.java.getDeclaredField("guildId").set(contact,uid)
        else if(uid == QQUtil.getCurrentUin()) contact::class.java.getDeclaredField("peerUid").set(contact,getUidFromUin(uid))
        else contact::class.java.getDeclaredField("peerUid").set(contact,uid)
        return contact
    }catch (ex: Exception) {
        LogCat.e("createContact错误",Util.getStackTraceString(ex))
        throw RuntimeException("create failed, since:"+ex)
    }
  }
  
  }
}

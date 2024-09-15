package com.luoyu.xposed.base

import java.lang.reflect.Method
import com.luoyu.utils.ClassUtil
import com.luoyu.utils.MethodUtil

import com.luoyu.xposed.base.HookEnv

class QRoute {
    companion object {
    
    @JvmStatic
    fun <T> api(clz: Class<*>) : T {
    val m = MethodUtil.create(ClassUtil.get("com.tencent.mobileqq.qroute.QRoute")).setMethodName("api").get(HookEnv.getHostClassLoader())
    try {
      return m.invoke(null, clz) as T
        } catch (e: Exception) {
            throw RuntimeException(e)
            }
        }
    }

}

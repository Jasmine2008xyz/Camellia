package com.luoyu.utils

import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import java.lang.reflect.Member
import java.util.concurrent.atomic.AtomicReference

object XposedBridge {
    const val Xposed_Bridge_Version=1;

    @JvmStatic
    fun invokeOriginalMethod(m: Member, obj: Any?,vararg objArr: Any?): Any? {
    return try{
        XposedBridge.invokeOriginalMethod(m,obj,objArr)
    } catch(e: Exception) {
        e.printStackTrace()
        null
        }
    }

    @JvmStatic
    fun hookMethod(m: Member, before: MethodHookParam_Before, after: MethodHookParam_After, pro: Int) {
        XposedBridge.hookMethod(m, object : XC_MethodHook(pro) {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                before.onBefore(param)
            }

            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                after.onAfter(param)
            }
        })
    }
    
    @JvmStatic
    fun hookMethod(m: Member, before: MethodHookParam_Before, after: MethodHookParam_After) {
        XposedBridge.hookMethod(m, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                before.onBefore(param)
            }

            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                after.onAfter(param)
            }
        })
    }

    @JvmStatic
    fun hookMethod_Before(m: Member, before: MethodHookParam_Before, pro: Int) {
        XposedBridge.hookMethod(m, object : XC_MethodHook(pro) {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                before.onBefore(param)
            }
        })
    }

    @JvmStatic
    fun hookMethod_Before(m: Member, before: MethodHookParam_Before) {
        XposedBridge.hookMethod(m, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                before.onBefore(param)
            }
        })
    }

    @JvmStatic
    fun hookMethod_After(m: Member, after: MethodHookParam_After, pro: Int) {
        XposedBridge.hookMethod(m, object : XC_MethodHook(pro) {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                after.onAfter(param)
            }
        })
    }

    @JvmStatic
    fun hookMethod_After(m: Member, after: MethodHookParam_After) {
        XposedBridge.hookMethod(m, object : XC_MethodHook() {
            @Throws(Throwable::class)
            override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                after.onAfter(param)
            }
        })
    }

    @JvmStatic
    fun hookMethod_Before_Once(m: Member, before: MethodHookParam_Before) {
        val cacheUnHook = AtomicReference<XC_MethodHook.Unhook>()
        cacheUnHook.set(
            XposedBridge.hookMethod(m, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                    val unhook = cacheUnHook.getAndSet(null)
                    if (unhook != null) {
                        unhook.unhook()
                        before.onBefore(param)
                    }
                }
            })
        )
    }

    @JvmStatic
    fun hookMethod_After_Once(m: Member, after: MethodHookParam_After) {
        val cacheUnHook = AtomicReference<XC_MethodHook.Unhook>()
        cacheUnHook.set(
            XposedBridge.hookMethod(m, object : XC_MethodHook() {
                @Throws(Throwable::class)
                override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam) {
                    val unhook = cacheUnHook.getAndSet(null)
                    if (unhook != null) {
                        unhook.unhook()
                        after.onAfter(param)
                    }
                }
            })
        )
    }

    // Interfaces remain unchanged as defined previously
    interface MethodHookParam_Before {
        @Throws(Throwable::class)
        fun onBefore(param: XC_MethodHook.MethodHookParam)
    }

    interface MethodHookParam_After {
        @Throws(Throwable::class)
        fun onAfter(param: XC_MethodHook.MethodHookParam)
    }
}

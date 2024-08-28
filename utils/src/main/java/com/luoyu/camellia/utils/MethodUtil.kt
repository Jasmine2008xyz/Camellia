package com.luoyu.camellia.utils

import de.robv.android.xposed.XposedHelpers

import java.lang.reflect.Method

class MethodUtil private constructor(clz: Class<*>) {
    lateinit var methodInfo: ReflectMethodInfo

    companion object {
        const val TAG = "MethodUtil"
        
        @JvmStatic
        fun create(clz: Class<*>): MethodUtil {
            return MethodUtil(clz)
        }
    }
    
    init {
        methodInfo = ReflectMethodInfo().apply {
            className = clz
        }
    }
    
    fun setMethodName(methodName: String) {
        methodInfo.methodName = methodName
    }
    
    fun setReturnType(returnType: Class<*>) {
        methodInfo.returnType = returnType
    }
    
    fun setParams(vararg params: Class<*>) {
        methodInfo.params = params
    }
    
    fun setParamsLength(len: Int) {
        methodInfo.paramsLength = len
    }
    
    fun get(loader: ClassLoader): Method {
        throw RuntimeException("")
    }
    
    fun call(loader: ClassLoader): Any {
        throw RuntimeException("")
    }
    
    class ReflectMethodInfo {
        var className: Class<*>? = null
        var methodName: String? = null
        var returnType: Class<*>? = null
        var params: Array<out Class<*>?>? = null
        
        var paramsLength: Int = -1
    }
}

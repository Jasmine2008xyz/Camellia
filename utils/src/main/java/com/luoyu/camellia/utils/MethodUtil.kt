package com.luoyu.camellia.utils

import de.robv.android.xposed.XposedHelpers

import java.lang.reflect.Method
import java.util.ArrayList

/**
 * @author luoyu
 * @date 2022/5/26
 */
class MethodUtil private constructor(clz: Class<*>) {
    var methodInfo: ReflectMethodInfo
    val methodsMap: HashMap<String,Method> = HashMap()

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
    
    fun setMethodName(methodName: String):  MethodUtil{
        methodInfo.methodName = methodName
        return this
    }
    
    fun setReturnType(returnType: Class<*>): MethodUtil {
        methodInfo.returnType = returnType
        return this
    }
    
    fun setParams(vararg params: Class<*>): MethodUtil {
        methodInfo.params = params
        return this
    }
    
    fun setParamsLength(len: Int): MethodUtil {
        methodInfo.paramsLength = len
        return this
    }
    
    fun get(loader: ClassLoader): Method {
        return get(loader,0)
    }
    
    fun get(loader: ClassLoader, count:Int = 0): Method {
        if(methodsMap.contains(methodInfo.toString())) return methodsMap.get(methodInfo.toString())!!
        val main = ArrayList<Method>()
        for (method in methodInfo.className!!.methods) {
            main.add(method)
        }
        for (method in methodInfo.className!!.methods) {
            if(method.name != methodInfo.methodName && methodInfo.methodName != null) main.remove(method)
            if(method.returnType != methodInfo.returnType && methodInfo.returnType != null) main.remove(method)
            if(method.parameterTypes.size != methodInfo.paramsLength && methodInfo.paramsLength != -1) main.remove(method)
            if(methodInfo.params != null) {
            for (i in 0..(methodInfo.params!!.size-1)) {
                if(!method.parameterTypes[i].equals(methodInfo.params!![i])) main.remove(method)
                }
            }
        }
        if(main.size == 1) {
            methodsMap.put(methodInfo.toString(), main[0])
            return main[count]
        } else if(main.size == 0) {
                    methodInfo.className = methodInfo.className!!.superclass
                    return get(loader)
                    } else {
                        /*
                        * 查找到的方法不唯一，这里放松条件，允许直接获取到第一位方法
                        */
                        methodsMap.put(methodInfo.toString(), main[0])
                        return main[count]
        }
    }
    
    fun call(loader: ClassLoader, instance: Any?, vararg args: Any?): Any? {
        try {
            return get(loader).invoke(instance,args)
        }catch(ex: Exception) {
            throw RuntimeException("Wrong in call method"+ex)
        }
    }
    
    fun call(loader: ClassLoader, count: Int, instance: Any?, vararg args: Any?): Any? {
        try {
            return get(loader,count).invoke(instance,args)
        }catch(ex: Exception) {
            throw RuntimeException("Wrong in call method"+ex)
        }
    }
    
    class ReflectMethodInfo {
        var className: Class<*>? = null
        var methodName: String? = null
        var returnType: Class<*>? = null
        var params: Array<out Class<*>?>? = null
        
        var paramsLength: Int = -1
    }
}

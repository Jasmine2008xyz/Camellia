package com.luoyu.xposed.logging

interface IQLog {
    fun setQLogPath(path: String)

    fun getQLogPath(): String

    fun log(TAG: String, ex: Any)
    
    fun d(TAG: String, ex: Any)
    
    fun e(TAG: String, ex: Any)
    
    fun i(TAG: String, ex: Any)
    
    fun w(TAG: String, ex: Any)
    
    fun t(TAG: String, ex: Any)
}

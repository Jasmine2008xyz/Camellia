package com.luoyu.camellia.interfaces
interface ILog {

    fun setQLogPath(path: String)

    fun getQLogPath(): String

    fun log(TAG: String, ex: Object)
    
    fun d(TAG: String, ex: Object)
    
    fun e(TAG: String, ex: Object)
    
    fun i(TAG: String, ex: Object)
    
    fun w(TAG: String, ex: Object)
    
    fun t(TAG: String, ex: Object)
}

package com.luoyu.camellia.utils

import com.luoyu.camellia.data.module.AppInfo

import com.luoyu.camellia.utils.ClassUtil

class Classes {
    
    companion object {
    
    fun getContactClass(): Class<*> {
        return Classes().getContactClass()
    }
    
    }
    /*
     * 获取qqnt下的nativeinterface.Cotact类
     * QQ9.0.70时qqnt下的kernel类进行了重新分类
     * 所以nativeinterface.Cotact在9.0.70上下分别有两个位置
     */
     
    fun getContactClass(): Class<*> {
        val versionCode = AppInfo.QQVersionCode
        if(versionCode >= 6676)/* QQ9.0.70 */
        return ClassUtil.get("com.tencent.qqnt.kernelpublic.nativeinterface.Contact")
        return ClassUtil.get("com.tencent.qqnt.kernel.nativeinterface.Contact")
    }
    
}

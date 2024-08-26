package com.luoyu.camellia.startup

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

import com.luoyu.camellia.utils.PathUtil

class Hook_Entry : IXposedHookLoadPackage, IXposedHookZygoteInit {

    private val TAG="Hook_Entry(钩子入口)"
    private val QQ_PACKAGE_NAME="com.tencent.mobileqq"

    // 实现 IXposedHookLoadPackage 接口的方法
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if(lpparam.packageName!=QQ_PACKAGE_NAME) return
        if(!lpparam.isFirstApplication) return
        // 处理包加载的逻辑
        HookInit(lpparam)
    }

    // 实现 IXposedHookZygoteInit 接口的方法
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        // 初始化 Zygote 的逻辑
        PathUtil.setModuleApkPath(startupParam.modulePath)
    }
}

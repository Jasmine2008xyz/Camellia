package com.luoyu.camellia.utils

import android.widget.Toast
import com.luoyu.camellia.app.CamelliaApp

import com.luoyu.xposed.base.HookEnv

import com.tencent.mobileqq.widget.QQToast

//把扩展函数放到这里面似乎不是很规范，但先这么写罢...
fun String.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(CamelliaApp.context, this, duration).show()
}

fun Int.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(CamelliaApp.context, this, duration).show()
}

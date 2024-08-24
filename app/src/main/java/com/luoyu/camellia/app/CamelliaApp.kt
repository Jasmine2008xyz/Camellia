package com.luoyu.camellia.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

//那个APP既然没用，就先不用了
class CamelliaApp: Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}
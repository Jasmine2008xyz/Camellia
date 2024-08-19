package com.luoyu.camellia.app;

import android.app.Application;
import android.content.res.Configuration;

public class App extends Application {

    @Override
    public void onCreate() {
        // 程序创建的时候执行
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 配置更改的时候执行
        super.onConfigurationChanged(newConfig);
    }
}

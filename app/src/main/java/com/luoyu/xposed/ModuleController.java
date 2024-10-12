package com.luoyu.xposed;
import android.os.HandlerThread;
import com.luoyu.config.MConfig;

/*
 * Created by luoyu
 * 用于取代原先的com.luoyu.camellia.base.MItem
 */
public class ModuleController {
    public static MConfig Config = new MConfig("Camellia");
    
    public static HandlerThread mTasker = new HandlerThread("Camellia_Handler_Main");
}

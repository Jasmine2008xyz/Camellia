package com.luoyu.dexfinder;


@Deprecated
public class DexClass {
    public static final String TAG = "DexClass";

    public String className = "";

    public DexClass(String str) {
        this.className = str;
    }
    
    public DexClass(Class<?> clz){
        this.className=clz.getName();
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}

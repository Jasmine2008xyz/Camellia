package com.luoyu.camellia.interfaces.jni;

public class Crypto {
    
    public static native String Encrypt(String str);
    
    public static native String Decode(String str);
    
}

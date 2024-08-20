package com.luoyu.camellia.interfaces;

/*
 * 本项目接口不要用kt写！！！
 */
public interface IDexFinder {
    void findMethodByUseString(String TAG, String[] SearchPackageName, String[] UseStrings);
    
    void findMethodsByUseAnnotation(String TAG,Class<?> UseAnnotation);
}

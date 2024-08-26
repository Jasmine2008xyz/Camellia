package com.luoyu.camellia.interfaces;

import java.util.ArrayList;

public interface IDexFinder {

    void findMethodsByPathAndUseString(
            String TAG[], String[] SearchPackageName, String[] UseStrings);

    /*
     * 查找自身模块类专用方法
     */
    void findSelfClassesByUseAnnotation(Class<?> UseAnnotation);

    void keepToFile();

    ArrayList<Class<?>> getModule_Object_List();
}

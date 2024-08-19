package com.luoyu.camellia.utils;

import de.robv.android.xposed.XposedHelpers;
import java.util.HashMap;

public class ClassUtil {
    public static HashMap<String, Class> ClassMap = new HashMap<>();

    public static ClassLoader loader;

    public static void InitClassLoader(ClassLoader cl) {
        loader = cl;
    }

    public static Class<?> load(String ClassName) {
        if (ClassMap.get(ClassName) != null) {
            return ClassMap.get(ClassName);
        } else {
            Class<?> Clazz = XposedHelpers.findClassIfExists(ClassName, loader);
            ClassMap.put(ClassName, Clazz);
            return Clazz;
        }
    }

    public static Class<?> caChe(Class<?> clazz) {
        ClassMap.put(clazz.getName(), clazz);
        return clazz;
    }

    public static Class<?> get(String ClassName) {
        return load(ClassName);
    }
}

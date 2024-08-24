package com.luoyu.camellia.utils;

import java.lang.reflect.Constructor;

public class ConstructorUtil {
    public static <T> T callConstrutor(Class CallClass, Class[] ParamsTYPE, Object... params) {
        try {
            Constructor cons = CallClass.getDeclaredConstructor(ParamsTYPE);
            cons.setAccessible(true);

            return (T) cons.newInstance(params);
        } catch (Exception err) {
            return null;
        }
    }

    public static <T> T callConstrutorNoParamType(Class<?> findClass, Object... params)
            throws Exception {
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        return callConstrutor(findClass, paramTypes, params);
    }

    /**
     * @Method 寻找实例 @Param clz 被调用的类名
     *
     * @param paramTypes 参数类型 @Return 实例(Constructor<?>)
     */
    public static Constructor<?> findCons(Class<?> clz, Class<?>[] paramTypes) {
        Loop:
        for (Constructor<?> con : clz.getDeclaredConstructors()) {
            Class<?>[] CheckParam = con.getParameterTypes();
            if (CheckParam.length != paramTypes.length) continue;
            for (int i = 0; i < paramTypes.length; i++) {
                if (!CheckClassType.CheckClass(CheckParam[i], paramTypes[i])) {
                    continue Loop;
                }
            }
            con.setAccessible(true);
            return con;
        }
        return null;
    }

    public static <T> T newInstance(Class<?> clz, Class<?>[] paramTypes, Object... params)
            throws Exception {
        Loop:
        for (Constructor<?> con : clz.getDeclaredConstructors()) {
            Class<?>[] CheckParam = con.getParameterTypes();
            if (CheckParam.length != paramTypes.length) continue;
            for (int i = 0; i < paramTypes.length; i++) {
                if (!CheckClassType.CheckClass(CheckParam[i], paramTypes[i])) {
                    continue Loop;
                }
            }
            con.setAccessible(true);
            return (T) con.newInstance(params);
        }
        throw new RuntimeException("No Instance for " + clz);
    }

    public static <T> T newInstance(Class<?> clz, Object... params) throws Exception {
        Class<?>[] paramTypes = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            paramTypes[i] = params[i].getClass();
        }
        return newInstance(clz, paramTypes, params);
    }

    public static Constructor<?> findConstructorByParamLength(Class<?> findClass, int length) {
        for (Constructor<?> constructor : findClass.getDeclaredConstructors()) {
            if (constructor.getParameterTypes().length == length) {
                constructor.setAccessible(true);
                return constructor;
            }
        }
        return null;
    }
}

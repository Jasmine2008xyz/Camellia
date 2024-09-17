package com.luoyu.utils.temp;

import com.luoyu.utils.ClassUtil;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class MMethod {
  public static <T> T CallMethod(
      Object CallObj, Class MethodClass, String MethodName, Class[] ParamTYPE, Object... params)
      throws Exception {
    String MethodSign = MethodClass.getName() + "->" + MethodName + "(NoReturn)[";
    for (Class clz : ParamTYPE) {
      MethodSign = MethodSign + clz.getName() + ";";
    }
    MethodSign = MethodSign + "]!Sign";

    if (MethodCache.containsKey(MethodSign)) {
      Method CallMethod = MethodCache.get(MethodSign);
      CallMethod.setAccessible(true);
      return (T) CallMethod.invoke(CallObj, params);
    }

    String[] MethodNameExtra = MethodName.split("\\|");
    List<String> lName = Arrays.asList(MethodNameExtra);

    Class TheCheckClass = MethodClass;
    while (TheCheckClass != null) {
      Method[] methods = TheCheckClass.getDeclaredMethods();
      MethodStart:
      for (Method mMethod : methods) {
        if (lName.contains(mMethod.getName())) {

          Class[] mParams = mMethod.getParameterTypes();
          if (ParamTYPE.length == mParams.length) {
            if (mParams.length == 0) {
              mMethod.setAccessible(true);
              return (T) mMethod.invoke(CallObj, params);
            }
            for (int i = 0; i < mParams.length; i++) {
              if (!mParams[i].equals(ParamTYPE[i])) continue MethodStart;
            }
            mMethod.setAccessible(true);
            return (T) mMethod.invoke(CallObj, params);
          }
        }
      }
      TheCheckClass = TheCheckClass.getSuperclass();
    }
    String ClassNameList = "";
    for (Class mClass : ParamTYPE) {
      ClassNameList = ClassNameList + "  " + mClass;
    }
    throw new Exception(
        "Can't find method " + MethodName + "(" + ClassNameList + ") in class " + MethodClass);
  }

  public static <T> T CallMethod(
      Object CallObj,
      Class MethodClass,
      String MethodName,
      Class ReturnType,
      Class[] ParamTYPE,
      Object... params)
      throws Exception {
    Class TheCheckClass = MethodClass;
    String MethodSign =
        MethodClass.getName() + "->" + MethodName + "(" + ReturnType.getName() + ")[";
    for (Class clz : ParamTYPE) {
      MethodSign = MethodSign + clz.getName() + ";";
    }
    MethodSign = MethodSign + "]!Sign";

    if (MethodCache.containsKey(MethodSign)) {
      Method CallMethod = MethodCache.get(MethodSign);
      CallMethod.setAccessible(true);
      return (T) CallMethod.invoke(CallObj, params);
    }

    String[] MethodNameExtra = MethodName.split("\\|");
    List<String> lName = Arrays.asList(MethodNameExtra);

    while (TheCheckClass != null) {
      Method[] methods = TheCheckClass.getDeclaredMethods();
      MethodStart:
      for (Method mMethod : methods) {
        if (lName.contains(mMethod.getName())) {
          if (mMethod.getReturnType().equals(ReturnType)) {
            Class[] mParams = mMethod.getParameterTypes();
            if (ParamTYPE.length == mParams.length) {
              if (mParams.length == 0) {
                mMethod.setAccessible(true);
                return (T) mMethod.invoke(CallObj, params);
              }
              for (int i = 0; i < mParams.length; i++) {
                if (!mParams[i].equals(ParamTYPE[i])) continue MethodStart;
              }
              mMethod.setAccessible(true);
              return (T) mMethod.invoke(CallObj, params);
            }
          }
        }
      }
      TheCheckClass = TheCheckClass.getSuperclass();
    }
    String ClassNameList = "";
    for (Class mClass : ParamTYPE) {
      ClassNameList = ClassNameList + "  " + mClass;
    }
    throw new Exception(
        "Can't find method " + MethodName + "(" + ClassNameList + ") in class " + MethodClass);
  }

  public static <T> T CallMethod(
      Object CallObj, String MethodName, Class ReturnType, Class[] ParamTYPE, Object... params)
      throws Exception {
    return CallMethod(CallObj, CallObj.getClass(), MethodName, ReturnType, ParamTYPE, params);
  }

  public static Method FindMethod(
      String ClassName, String MethodName, Class ReturnType, Class[] ParamTYPE) throws Exception {
    return FindMethod(ClassUtil.load(ClassName), MethodName, ReturnType, ParamTYPE);
  }

  private static HashMap<String, Method> MethodCache = new HashMap<>();

  public static Method FindMethod(
      Class MethodClass, String MethodName, Class ReturnType, Class[] ParamTYPE) throws Exception {

    String MethodSign =
        MethodClass.getName() + "->" + MethodName + "(" + ReturnType.getName() + ")[";
    for (Class clz : ParamTYPE) {
      MethodSign = MethodSign + clz.getName() + ";";
    }
    MethodSign = MethodSign + "]!Sign";
    if (MethodCache.containsKey(MethodSign)) return MethodCache.get(MethodSign);

    String[] MethodNameExtra = MethodName.split("\\|");
    List<String> lName = Arrays.asList(MethodNameExtra);

    Method[] methods = MethodClass.getDeclaredMethods();
    MethodStart:
    for (Method mMethod : methods) {
      if (lName.contains(mMethod.getName())) {
        if (mMethod.getReturnType().equals(ReturnType)) {
          Class[] mParams = mMethod.getParameterTypes();
          if (ParamTYPE.length == mParams.length) {
            if (mParams.length == 0) {
              MethodCache.put(MethodSign, mMethod);
              return mMethod;
            }
            for (int i = 0; i < mParams.length; i++) {
              if (!mParams[i].equals(ParamTYPE[i])) continue MethodStart;
            }
            MethodCache.put(MethodSign, mMethod);
            return mMethod;
          }
        }
      }
    }
    String ClassNameList = "";
    for (Class mClass : ParamTYPE) {
      ClassNameList = ClassNameList + mClass;
    }
    throw new Exception(
        "Can't find method "
            + ReturnType
            + " "
            + MethodName
            + "("
            + ClassNameList
            + ") in "
            + MethodClass);
  }

  public static Method _FindMethod(
      Class MethodClass, String MethodName, Class ReturnType, Class[] ParamTYPE) {

    String[] MethodNameExtra = MethodName.split("\\|");
    List<String> lName = Arrays.asList(MethodNameExtra);

    Method[] methods = MethodClass.getDeclaredMethods();
    MethodStart:
    for (Method mMethod : methods) {
      if (lName.contains(mMethod.getName())) {
        if (mMethod.getReturnType().equals(ReturnType)) {
          Class[] mParams = mMethod.getParameterTypes();
          if (ParamTYPE.length == mParams.length) {
            if (mParams.length == 0) {
              return mMethod;
            }
            for (int i = 0; i < mParams.length; i++) {
              if (!mParams[i].equals(ParamTYPE[i])) continue MethodStart;
            }
            return mMethod;
          }
        }
      }
    }
    String ClassNameList = "";
    for (Class mClass : ParamTYPE) {
      ClassNameList = ClassNameList + mClass;
    }
    return null;
  }

  public static Method FindFirstMethod(Class clz, Class ReturnType, Class[] ParamTYPE) {
    Lopp:
    for (Method method : clz.getDeclaredMethods()) {
      if (method.getParameterCount() == ParamTYPE.length) {
        Class[] params = method.getParameterTypes();
        for (int i = 0; i < method.getParameterCount(); i++) {
          if (!params[i].equals(ParamTYPE[i])) continue Lopp;
        }

        if (method.getReturnType().equals(ReturnType)) return method;
      }
    }
    return null;
  }

  public static Method FindMethod(String ClassName, String MethodName, Class ReturnType)
      throws Exception {
    return FindMethod(ClassName, MethodName, ReturnType, new Class[0]);
  }

  public static Method FindMethod(Class ClassInter, String MethodName, Class ReturnType)
      throws Exception {
    return FindMethod(ClassInter, MethodName, ReturnType, new Class[0]);
  }
}

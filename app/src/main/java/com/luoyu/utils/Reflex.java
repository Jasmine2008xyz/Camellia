package com.luoyu.utils;

import android.util.Log;
import androidx.annotation.NonNull;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class Reflex {
  private static final String TAG = "Reflex";

  private Reflex() {
    throw new RuntimeException("This class has no instance!");
  }

  /*
   * Java反射工具
   * 使用前先为 {@link #hostClassLoader} 赋值
   */

  private static ClassLoader hostClassLoader;

  /*
   * getter/setter for {@link #hostClassLoader}
   */

  public static ClassLoader getHostClassLoader() {
    return hostClassLoader;
  }

  public static void setHostClassLoader(ClassLoader hostLoader) {
    hostClassLoader = hostLoader;
  }

  /*
   * Class<?> loadClass(String)
   * Get class instance from hostClassLoader
   */

  public static Class<?> loadClass(@NonNull String clz) {
    try {
      if (clz.contains("/")) clz = clz.replace("/", ".");
      if (clz.startsWith("L")) clz = clz.substring(1, clz.length() - 1);
      return hostClassLoader.loadClass(clz);
    } catch (Exception e) {
      throw new RuntimeException("未在QQ中找到此类：" + clz + "\n" + Log.getStackTraceString(e));
    }
  }

  /*
   * Class<?> _loadClass(String)
   * Get class instance from self
   */

  public static Class<?> _loadClass(@NonNull String clz) {
    try {
      if (clz.contains("/")) clz = clz.replace("/", ".");
      if (clz.startsWith("L")) clz = clz.substring(1, clz.length() - 1);
      return Reflex.class.getClassLoader().loadClass(clz);
    } catch (Exception e) {
      throw new RuntimeException("未在模块中找到此类：" + clz + "\n" + Log.getStackTraceString(e));
    }
  }

  public static MethodMatcher findMethod(Class clz) {
    return MethodMatcher.create(clz);
  }

  public static class MethodMatcher {
    public static HashMap<String, Method> method_Map = new HashMap<>();
    public ReflexMethod reflexMethod;

    public MethodMatcher(@NonNull Class<?> clz) {
      this.reflexMethod = new ReflexMethod();
      this.reflexMethod.setClassName(new Class[] {clz});
    }

    public MethodMatcher(@NonNull Class<?>[] clz) {
      this.reflexMethod = new ReflexMethod();
      this.reflexMethod.setClassName(clz);
    }

    public static MethodMatcher create(@NonNull Class<?> clz) {
      return new MethodMatcher(clz);
    }

    public static MethodMatcher create(@NonNull Class<?>[] clz) {
      return new MethodMatcher(clz);
    }

    public static MethodMatcher create(@NonNull String clz) {
      return new MethodMatcher(loadClass(clz));
    }

    public MethodMatcher setClassName(@NonNull Class<?> clz) {
      this.reflexMethod.setClassName(new Class[] {clz});
      return this;
    }

    public MethodMatcher setMethodName(@NonNull String methodName) {
      this.reflexMethod.setMethodName(methodName);
      return this;
    }

    public MethodMatcher setReturnType(@NonNull Class<?> clz) {
      this.reflexMethod.setReturnType(clz);
      return this;
    }

    public MethodMatcher setReturnType(@NonNull String clz) {
      this.reflexMethod.setReturnType(loadClass(clz));
      return this;
    }

    public MethodMatcher setParams(@NonNull Class<?>... clz) {
      this.reflexMethod.setParams(clz);
      return this;
    }

    public MethodMatcher setParamsLength(int count) {
      this.reflexMethod.setParamsLength(count);
      return this;
    }

    public ReflexMethod getReflexMethod() {
      return this.reflexMethod;
    }

    public MethodMatcher setReflexMethod(@NonNull ReflexMethod reflexMethod) {
      this.reflexMethod = reflexMethod;
      return this;
    }

    /*
     * Method get()
     * 获取匹配的方法
     */
    public Method get() {
      if (method_Map.containsKey(this.reflexMethod.toString())) {
        return method_Map.get(this.reflexMethod.toString());
      } else if (this.reflexMethod.getClassName() == null) {
        throw new RuntimeException("Class name canot be null!");
      } else {
        ArrayList<Method> temp = new ArrayList<>();
        for (Class<?> clz : this.reflexMethod.className) {
          for (Method m : clz.getDeclaredMethods()) {
            temp.add(m);
            if (this.reflexMethod.getMethodName() != null
                && !this.reflexMethod.getMethodName().equals(m.getName())) temp.remove(m);
            if (this.reflexMethod.getReturnType() != null
                && !this.reflexMethod.getReturnType().equals(m.getReturnType())) temp.remove(m);
            if (this.reflexMethod.getParamsLength() != -1
                && this.reflexMethod.getParamsLength() != m.getParameterCount()) temp.remove(m);
            if (this.reflexMethod.getParams() != null
                && this.reflexMethod.getParams().length == m.getParameterCount()) {
              int i = 0;
              for (Class<?> clazz : this.reflexMethod.getParams()) {
                if (!clazz.equals(m.getParameterTypes()[i]) && clazz != Object.class) {
                  temp.remove(m);
                  i++;
                  break;
                }
              }
            }
          }
        }
        if (temp.size() == 0) {
          this.reflexMethod.setClassName(
              new Class[] {this.reflexMethod.getClassName()[0].getSuperclass()});
          return get();
        } else {
          return temp.get(0);
        }
      }
    }
  }

  public static class ReflexMethod {
    public Class<?>[] className;
    public String methodName;
    public Class<?> returnType;
    public Class<?>[] params;

    public int paramsLength = -1;

    public ReflexMethod(
        Class<?>[] className,
        String methodName,
        Class<?> returnType,
        Class<?>[] params,
        int paramsLength) {
      this.className = className;
      this.methodName = methodName;
      this.returnType = returnType;
      this.params = params;
      this.paramsLength = paramsLength;
    }

    public ReflexMethod() {}

    public Class<?>[] getClassName() {
      return this.className;
    }

    public void setClassName(Class<?>[] className) {
      this.className = className;
    }

    public String getMethodName() {
      return this.methodName;
    }

    public void setMethodName(String methodName) {
      this.methodName = methodName;
    }

    public Class<?> getReturnType() {
      return this.returnType;
    }

    public void setReturnType(Class<?> returnType) {
      this.returnType = returnType;
    }

    public Class<?>[] getParams() {
      return this.params;
    }

    public void setParams(Class<?>[] params) {
      this.params = params;
    }

    public int getParamsLength() {
      return this.paramsLength;
    }

    public void setParamsLength(int paramsLength) {
      this.paramsLength = paramsLength;
    }
  }
  @Override
    public String toString () {
      return null;
    }

}

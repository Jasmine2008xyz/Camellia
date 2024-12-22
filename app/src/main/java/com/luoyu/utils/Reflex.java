package com.luoyu.utils;

import android.util.Log;
import androidx.annotation.NonNull;
import java.lang.reflect.Field;
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
      return null;
      // throw new RuntimeException("未在QQ中找到此类：" + clz + "\n" + Log.getStackTraceString(e));
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

  public static FieldMatcher findField(Class clz) {
    return FieldMatcher.create(clz);
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
            if (this.reflexMethod.getParams() != null) {
              if (this.reflexMethod.getParams().length != m.getParameterCount()) temp.remove(m);
              else {
                int i = 0;
                for (Class<?> clazz : this.reflexMethod.getParams()) {
                  if ((!clazz.equals(m.getParameterTypes()[i])) && (!clazz.equals(Object.class))) {
                    temp.remove(m);
                    break;
                  }
                  i++;
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
          var result = temp.get(0);
          result.setAccessible(true);
          return result;
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

    @Override
    public String toString() {
      return "ReflexMethod[className="
          + className
          + ", methodName="
          + methodName
          + ", returnType="
          + returnType
          + ", params="
          + params
          + ", paramsLength="
          + paramsLength
          + "]";
    }
  }

  public static class FieldMatcher {
    public static HashMap<String, Field> field_Map = new HashMap<>();
    public ReflexField reflexField;

    public FieldMatcher(@NonNull Class<?> clz) {
      this.reflexField = new ReflexField();
      this.reflexField.setClassName(new Class[] {clz});
    }

    public static FieldMatcher create(@NonNull Class<?> clz) {
      return new FieldMatcher(clz);
    }

    public static FieldMatcher create(@NonNull String clz) {
      return new FieldMatcher(loadClass(clz));
    }

    public FieldMatcher setClassName(@NonNull Class<?> clz) {
      this.reflexField.setClassName(new Class[] {clz});
      return this;
    }

    public FieldMatcher setFieldName(@NonNull String fieldName) {
      this.reflexField.setFieldName(fieldName);
      return this;
    }

    public FieldMatcher setReturnType(@NonNull Class<?> clz) {
      this.reflexField.setReturnType(clz);
      return this;
    }

    public FieldMatcher setReturnType(@NonNull String clz) {
      this.reflexField.setReturnType(loadClass(clz));
      return this;
    }

    public Class<?> getReturnType() {
      return this.reflexField.getReturnType();
    }

    public ReflexField getReflexField() {
      return this.reflexField;
    }

    public FieldMatcher setReflexField(@NonNull ReflexField reflexField) {
      this.reflexField = reflexField;
      return this;
    }

    /*
     * Field get()
     * 获取匹配的字段
     */
    public Field get() {
      if (field_Map.containsKey(this.reflexField.toString())) {
        return field_Map.get(this.reflexField.toString());
      } else if (this.reflexField.getClassName() == null) {
        throw new RuntimeException("Class name canot be null!");
      } else {
        ArrayList<Field> temp = new ArrayList<>();
        for (Class<?> clz : this.reflexField.className) {
          for (Field f : clz.getDeclaredFields()) {
            temp.add(f);
            if (this.reflexField.getFieldName() != null
                && !this.reflexField.getFieldName().equals(f.getName())) temp.remove(f);
            if (this.reflexField.getReturnType() != null
                && !this.reflexField.getReturnType().equals(f.getType())) temp.remove(f);
          }
        }
        if (temp.size() == 0) {
          this.reflexField.setClassName(
              new Class[] {this.reflexField.getClassName()[0].getSuperclass()});
          return get();
        } else {
          var result = temp.get(0);
          result.setAccessible(true);
          return result;
        }
      }
    }
  }

  public static class ReflexField {
    public Class<?>[] className;
    public String fieldName;
    public Class<?> returnType;

    public ReflexField(Class<?>[] className, String fieldName, Class<?> returnType) {
      this.className = className;
      this.fieldName = fieldName;
      this.returnType = returnType;
    }

    public ReflexField() {}

    public Class<?>[] getClassName() {
      return this.className;
    }

    public void setClassName(Class<?>[] className) {
      this.className = className;
    }

    public String getFieldName() {
      return this.fieldName;
    }

    public void setFieldName(String fieldName) {
      this.fieldName = fieldName;
    }

    public Class<?> getReturnType() {
      return this.returnType;
    }

    public void setReturnType(Class<?> returnType) {
      this.returnType = returnType;
    }

    @Override
    public String toString() {
      return "ReflexField[className="
          + className
          + ", fieldName="
          + fieldName
          + ", returnType="
          + returnType
          + "]";
    }
  }
}

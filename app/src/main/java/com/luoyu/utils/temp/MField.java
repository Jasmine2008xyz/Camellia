package com.luoyu.utils.temp;

import java.lang.reflect.Field;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class MField {
  private static HashMap<String, Field> FieldCache = new HashMap<>();

  public static String CalcFieldSign(Class CheckClass, String FieldName, Class Type) {
    String mSign = CheckClass.getName() + "->" + FieldName + "(" + Type.getName() + ")!Sign";
    return mSign;
  }

  public static <T> T GetField(Object obj, String Name) throws Exception {

    String FieldSign = CalcFieldSign(obj.getClass(), Name + "!!NOType", void.class);
    if (FieldCache.containsKey(FieldSign)) {
      Field mGetField = FieldCache.get(FieldSign);
      return (T) mGetField.get(obj);
    }

    if (obj == null) throw new Exception("No Such Field " + Name + " in NULL Object");
    Class mClass = obj.getClass();
    for (int i = 0; i < 99; i++) {
      Field[] mFields = mClass.getDeclaredFields();
      for (Field CheckField : mFields) {
        if (CheckField.getName().equals(Name)) {
          CheckField.setAccessible(true);
          FieldCache.put(FieldSign, CheckField);
          return (T) CheckField.get(obj);
        }
      }
      mClass = mClass.getSuperclass();
      if (mClass == null) {
        throw new Exception("No Such Field " + Name + " in " + obj.getClass());
      }
    }
    throw new Exception("No Such Field " + Name + " in " + obj.getClass());
  }

  public static <T> T GetField(Object FieldObj, Class CheckClass, String FieldName, Class Type)
      throws Exception {
    String FieldSign = CalcFieldSign(CheckClass, FieldName, Type);
    if (FieldCache.containsKey(FieldSign)) {
      Field mGetField = FieldCache.get(FieldSign);
      return (T) mGetField.get(FieldObj);
    }

    String[] MethodNameExtra = FieldName.split("\\|");
    List<String> lName = Arrays.asList(MethodNameExtra);

    Class mClass = CheckClass;
    while (mClass != null) {
      Field[] mFields = mClass.getDeclaredFields();
      for (Field CheckField : mFields) {
        if (lName.contains(CheckField.getName())) {
          if (CheckField.getType().equals(Type)) {
            CheckField.setAccessible(true);
            FieldCache.put(FieldSign, CheckField);
            return (T) CheckField.get(FieldObj);
          }
        }
      }
      mClass = mClass.getSuperclass();
      if (mClass == null) {
        throw new Exception("No Such Field " + FieldName + "(" + Type + ") in " + CheckClass);
      }
    }
    throw new Exception("No Such Field " + FieldName + "(" + Type + ") in " + CheckClass);
  }

  public static <T> T GetField(Object FieldObj, String FieldName, Class Type) throws Exception {
    return GetField(FieldObj, FieldObj.getClass(), FieldName, Type);
  }

  public static <T> T GetFirstField(Object FieldObj, Class CheckClass, Class FieldType)
      throws Exception {
    String FieldSign = CalcFieldSign(CheckClass, "!NoName!", FieldType);
    if (FieldCache.containsKey(FieldSign)) {
      Field mGetField = FieldCache.get(FieldSign);
      return (T) mGetField.get(FieldObj);
    }

    for (Field mfield : CheckClass.getDeclaredFields()) {
      if (mfield.getType().equals(FieldType)) {
        mfield.setAccessible(true);
        FieldCache.put(FieldSign, mfield);
        return (T) mfield.get(FieldObj);
      }
    }
    throw new NoSuchFieldException("Can't find field type " + FieldType + " in " + CheckClass);
  }

  public static <T> T GetField(Object FieldObj, Class CheckClass, String FieldName, int CircleRound)
      throws Exception {
    String FieldSign = CalcFieldSign(CheckClass, FieldName, AsynchronousServerSocketChannel.class);
    if (FieldCache.containsKey(FieldSign)) {
      Field mGetField = FieldCache.get(FieldSign);
      return (T) mGetField.get(FieldObj);
    }
    Class mClass = CheckClass;
    for (int i = 0; i < CircleRound; i++) {
      Field[] mFields = mClass.getDeclaredFields();
      for (Field CheckField : mFields) {
        if (CheckField.getName().equals(FieldName)) {
          CheckField.setAccessible(true);
          FieldCache.put(FieldSign, CheckField);
          return (T) CheckField.get(FieldObj);
        }
      }
      mClass = mClass.getSuperclass();
      if (mClass == null) {
        throw new Exception("No Such Field " + FieldName + " in " + CheckClass);
      }
    }
    throw new Exception("No Such Field " + FieldName + " in " + CheckClass);
  }

  public static Class getSimpleType(Class mClass) {
    try {
      return GetField(null, mClass, "TYPE", Class.class);
    } catch (Exception e) {
      return mClass;
    }
  }

  public static <T> void SetField(Object setObj, String FieldName, T SetValue) throws Exception {
    SetField(setObj, setObj.getClass(), FieldName, SetValue);
  }

  public static <T> void SetField(Object setObj, Class ObjClass, String FieldName, T SetValue)
      throws Exception {
    if (SetValue != null) {
      SetField(setObj, ObjClass, FieldName, SetValue, getSimpleType(SetValue.getClass()));
    }
  }

  public static <T> void SetField(
      Object setObj, Class ObjClass, String FieldName, T SetValue, Class SetTYPE) throws Exception {
    String FieldSign = CalcFieldSign(ObjClass, FieldName, SetTYPE);
    if (FieldCache.containsKey(FieldSign)) {
      Field mGetField = FieldCache.get(FieldSign);
      mGetField.set(setObj, SetValue);
      return;
    }

    Class CheckClz = ObjClass;
    while (CheckClz != null) {
      Field[] fields = CheckClz.getDeclaredFields();
      for (Field mfield : fields) {
        if (mfield.getName().equals(FieldName)) {
          if (mfield.getType().equals(SetTYPE) || mfield.getType().isAssignableFrom(SetTYPE)) {
            mfield.setAccessible(true);
            FieldCache.put(FieldSign, mfield);
            try {
              if (SetTYPE.equals(int.class)) {
                mfield.setInt(setObj, (Integer) SetValue);
                return;
              } else if (SetTYPE.equals(short.class)) {
                mfield.setShort(setObj, (Short) SetValue);
                return;
              } else if (SetTYPE.equals(byte.class)) {
                mfield.setByte(setObj, (Byte) SetValue);
                return;
              } else if (SetTYPE.equals(char.class)) {
                mfield.setChar(setObj, (Character) SetValue);
                return;
              } else if (SetTYPE.equals(long.class)) {
                mfield.setLong(setObj, (Long) SetValue);
                return;
              } else if (SetTYPE.equals(boolean.class)) {
                mfield.setBoolean(setObj, (Boolean) SetValue);
                return;
              } else if (SetTYPE.equals(float.class)) {
                mfield.setFloat(setObj, (Float) SetValue);
                return;
              } else if (SetTYPE.equals(double.class)) {
                mfield.setDouble(setObj, (Double) SetValue);
                return;
              } else {
                mfield.set(setObj, SetValue);
                return;
              }
            } catch (Exception ex) {
              throw ex;
            }
          }
        }
      }
      CheckClz = CheckClz.getSuperclass();
    }
    throw new Exception("No Such Field " + FieldName + " in " + ObjClass);
  }
}

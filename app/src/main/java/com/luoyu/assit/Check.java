package com.luoyu.assit;

public class Check {
    private static final String TAG = "Check(检验)";

    public static final boolean CheckIsNull(Object obj) {
        if (obj == null) return true;
        else return false;
    }

    public static final boolean CheckIsEmpty(String str) {
        if (str == null || str.equals("")) return true;
        else return false;
    }

    public static final boolean CheckIsEquals(Object obj1, Object obj2) {
        if (obj1 != null) {
            if (obj1.equals(obj2)) return true;
            else return false;
        } else {
            if (obj2 == null) {
                return true;
            }
            return false;
        }
    }
}

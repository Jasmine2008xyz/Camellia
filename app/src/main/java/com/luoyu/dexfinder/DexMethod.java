package com.luoyu.dexfinder;

import androidx.annotation.NonNull;
import com.luoyu.camellia.interfaces.IDexMember;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public final class DexMethod implements Serializable, IDexMember {

    public static final String TAG = "DexMethod";

    /*
     * 方法类基本字段
     */

    public String className = "";
    public String methodName = "";
    public String returnType = "";
    public String[] params;

    /*
     * 特殊查找需求字段
     */

    public int paramsLength;

    /*
     * 与查找无关的字段
     */

    public int methodId;

    public DexMethod(@NonNull Method method) {
        this.className = method.getDeclaringClass().getName();
        this.methodName = method.getName();
        this.returnType = method.getReturnType().getName();
        int temp = 0;
        for (Class<?> param : method.getParameterTypes()) {
            params[temp] = param.getDeclaringClass().getName();
            temp++;
        }
        this.paramsLength = temp;
        this.methodId = ThreadLocalRandom.current().nextInt(0, 100000000);
    }

    public DexMethod(String str, String str1, String str2, String[] str3, int i) {
        this.className = str;
        this.methodName = str1;
        this.returnType = str2;
        this.params = str3;
        this.paramsLength = i;
        this.methodId = ThreadLocalRandom.current().nextInt(0, 100000000);
    }

    @Override
    public String toString() {
        ArrayList<String> paramsList = new ArrayList<>();
        for (String param : params) {
            paramsList.add(param);
        }
        return "DexMethod(className="
                + className
                + ",methodName="
                + methodName
                + ",returnType="
                + returnType
                + ",params="
                + paramsList
                + ",paramsList="
                + paramsLength
                + ",methodId="
                + methodId
                + ")";
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return toString().hashCode() + methodId;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getReturnType() {
        return this.returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String[] getParams() {
        return this.params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public int getParamsLength() {
        return this.paramsLength;
    }

    public void setParamsLength(int paramsLength) {
        this.paramsLength = paramsLength;
    }

    public int getMethodId() {
        return this.methodId;
    }

    public void setMethodId(int methodId) {
        this.methodId = methodId;
    }
}

/*package com.luoyu.dexfinder

import java.io.Serializable
import java.lang.reflect.Method

class DexMethod : Serializable {
    private val TAG = "DexMethod(索引方法)"

    lateinit var className: String
    lateinit var methodName: String
    lateinit var returnType: String
    lateinit var params: Array<String>





    override fun toString(): String {
        return "DexMethod(className='$className', methodName='$methodName', returnType='$returnType', params=${params.joinToString()})"
    }
}*/

package com.luoyu.dexfinder;

import com.luoyu.utils.FileUtil;
import com.luoyu.utils.PathUtil;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.luckypray.dexkit.DexKitBridge;
import org.luckypray.dexkit.query.FindClass;
import org.luckypray.dexkit.query.FindMethod;
import org.luckypray.dexkit.query.matchers.AnnotationMatcher;
import org.luckypray.dexkit.query.matchers.ClassMatcher;
import org.luckypray.dexkit.query.matchers.MethodMatcher;
import org.luckypray.dexkit.result.ClassData;
import org.luckypray.dexkit.result.ClassDataList;
import org.luckypray.dexkit.result.MethodData;
import org.luckypray.dexkit.result.MethodDataList;

public class DexKitFinder implements IDexFinder {
    private static final String TAG = "DexKitFinder";
    private DexKitBridge bridge;
    private ClassLoader loader;
    //  private ArrayList<Method> QQ_Object_List = new ArrayList<>();
    private HashMap<Method, String> QQ_Object_HashMap = new HashMap<>();
    public ArrayList<Class<?>> Module_Object_List = new ArrayList<>();

    public DexKitFinder(ClassLoader cl, String apkPath) {
        this.loader = cl;
        this.bridge = DexKitBridge.create(apkPath);
    }

    static {
        SoLoader.loadByName("libdexkit.so");
    }

    @Override
    public void findMethodsByPathAndUseString(
            String TAG[], String[] SearchPackageName, String[] UseStrings) {
        try {
            MethodDataList list =
                    bridge.findMethod(
                            FindMethod.create()
                                    .searchPackages(SearchPackageName)
                                    .matcher(MethodMatcher.create().usingStrings(UseStrings)));
            int i = 0;
            for (String tag : TAG) {
                QQ_Object_HashMap.put(list.get(i).getMethodInstance(loader), tag);
                i++;
            }
            //  for (MethodData data : list) {
            //      QQ_Object_List.add(data.getMethodInstance(loader));
            //   }

        } catch (Exception err) {
            throw new RuntimeException(err);
        }
    }

    @Override
    public void findSelfClassesByUseAnnotation(Class<?> UseAnnotation) {
        try {
            ClassDataList list =
                    bridge.findClass(
                            FindClass.create()
                                    .matcher(
                                            ClassMatcher.create()
                                                    .addAnnotation(
                                                            AnnotationMatcher.create()
                                                                    .type(UseAnnotation))));
            for (ClassData data : list) {
                Module_Object_List.add(data.getInstance(loader));
            }
        } catch (Exception err) {
            throw new RuntimeException(err);
        }
    }

    public void keepToFile() {
        if (QQ_Object_HashMap.size() != 0) {
            JSONObject root = new JSONObject();
            QQ_Object_HashMap.forEach(
                    (key, value) -> {
                        var json = new JSONObject();
                        try {
                            json.put("className", key.getDeclaringClass().getName());
                            json.put("methodName", key.getName());
                            json.put("returnType", key.getReturnType().getName());
                            json.put("paramsLength", key.getParameterCount());
                            int i = 0;
                            for (Class<?> clz : key.getParameterTypes()) {
                                json.put("params_" + i, clz.getName());
                                i++;
                            }
                            root.put(value, json);
                        } catch (Exception err) {

                        }
                    });
            FileUtil.writeToFile(PathUtil.getApkDataPath() + "QQ_Object_Data", root.toString());
        }
        if (Module_Object_List.size() != 0) {
            JSONObject root = new JSONObject();
            for (int i = 0; i < Module_Object_List.size(); ++i) {
                try {
                    root.put("class_" + i, Module_Object_List.get(i).getName());
                } catch (Exception err) {

                }
            }
            FileUtil.writeToFile(PathUtil.getApkDataPath() + "Module_Object_Data", root.toString());
        }
    }

    @Override
    public ArrayList<Class<?>> getModule_Object_List() {
        return this.Module_Object_List;
    }

    public void setModule_Object_List(ArrayList<Class<?>> Module_Object_List) {
        this.Module_Object_List = Module_Object_List;
    }
}

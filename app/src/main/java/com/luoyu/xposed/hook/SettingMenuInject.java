package com.luoyu.xposed.hook;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.luoyu.camellia.activities.SettingsActivity;
import com.luoyu.xposed.base.annotations.Xposed_Item_Controller;
import com.luoyu.xposed.base.annotations.Xposed_Item_Entry;
import com.luoyu.xposed.base.HookEnv;
import com.luoyu.xposed.ModuleController;
import com.luoyu.xposed.logging.LogCat;
import com.luoyu.xposed.startup.DexFinderProcessor;
import com.luoyu.xposed.startup.HookInit;
import com.luoyu.utils.ClassUtil;
import com.luoyu.utils.ConstructorUtil;
import com.luoyu.utils.FileUtil;
import com.luoyu.utils.PathUtil;
import com.luoyu.utils.XRes;

import com.luoyu.xposed.ModuleController;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

@Xposed_Item_Controller(isApi = true)
/*
 * 思路: QStory(已经闭源)
 */
public class SettingMenuInject {

    static Object ItemCache = null;
    public static final String TAG = "SettingInject(设置界面注入入口)";
    public static Drawable RepeatIconBitmap;

    @Xposed_Item_Entry
    @SuppressWarnings("unchecked")
    public void start() throws Exception {
        if (ModuleController.Config.getBooleanData("模块设置/关闭设置界面注入入口", false)) return;
        Method method =
                XposedHelpers.findMethodsByExactParameters(
                        ClassUtil.get(
                                "com.tencent.mobileqq.setting.main.MainSettingConfigProvider"),
                        List.class,
                        new Class[] {Context.class})[0];
        XposedBridge.hookMethod(
                method,
                new XC_MethodHook(99) {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        Context context = (Context) param.args[0];
                        XRes.addAssetsPath(context);

                        // 获取方法的返回结果 item组包装器List-结构和当前类的DemoItemGroupWraper类似
                        Object result = param.getResult();
                        List<Object> itemGroupWraperList = (List<Object>) result;
                        // 获取返回的集合泛类型
                        Class<?> itemGroupWraperClass = itemGroupWraperList.get(0).getClass();
                        // 循环包装器组集合 目的是获取里面的元素
                        for (Object wrapper : itemGroupWraperList) {
                            try {
                                // 获取包装器里实际存放的Item集合

                                List itemList =
                                        (List)
                                                XposedHelpers.findFirstFieldByExactType(
                                                                wrapper.getClass(), List.class)
                                                        .get(wrapper);
                                // 筛选

                                if (itemList == null || itemList.isEmpty()) continue;
                                String name = itemList.get(0).getClass().getName();

                                if (!name.startsWith("com.tencent.mobileqq.setting.processor"))
                                    continue;
                                // 获取itemList的首个元素并取得Class
                                final Class<?> itemClass = itemList.get(0).getClass();
                                // 新建自己的Item
                                String title;
                                if (FileUtil.readFileString(PathUtil.getApkDataPath() + "Sign")
                                                == null
                                        || !HookInit.getSign()
                                                .equals(
                                                        FileUtil.readFileString(
                                                                PathUtil.getApkDataPath() + "Sign")))
                                    title = "Camellia[未激活]";
                                else title = "Camellia";
                                Object mItem =
                                        ConstructorUtil.newInstance(
                                                itemClass,
                                                new Class[] {
                                                    Context.class,
                                                    int.class,
                                                    CharSequence.class,
                                                    int.class
                                                },
                                                context,
                                                0x520a,
                                                title,
                                                com.luoyu.camellia.R.drawable.ic_menu_slideshow);
                                Method[] setOnClickMethods =
                                        fuzzyLookupMethod(
                                                itemClass,
                                                new FuzzyLookupConditions() {
                                                    @Override
                                                    public boolean isItCorrect(
                                                            Method currentMethod) {
                                                        // 在这个类查找所有符合 public void ?(Function0
                                                        // function0)的方法 可以查找到两个 一个是点击事件
                                                        // 一个是item刚被初始化时的事件
                                                        try {
                                                            return currentMethod.getReturnType()
                                                                            == void.class
                                                                    && (currentMethod
                                                                                            .getParameterTypes()
                                                                                            .length
                                                                                    == 1
                                                                            && currentMethod
                                                                                    .getParameterTypes()[
                                                                                    0]
                                                                                    .equals(
                                                                                            ClassUtil
                                                                                                    .load(
                                                                                                            "kotlin.jvm.functions.Function0")));
                                                        } catch (Exception e) {
                                                            return false;
                                                        }
                                                    }
                                                });
                                // 动态代理设置事件
                                Object onClickListener =
                                        Proxy.newProxyInstance(
                                                HookEnv.getHostClassLoader(),
                                                new Class[] {
                                                    ClassUtil.load("kotlin.jvm.functions.Function0")
                                                },
                                                new InvocationHandler() {
                                                    @Override
                                                    public Object invoke(
                                                            Object proxy,
                                                            Method method,
                                                            Object[] args) {
                                                        Throwable throwable = new Throwable();
                                                        StackTraceElement[] stackTraceElements =
                                                                throwable.getStackTrace();
                                                        // 通过调用栈来确定是不是被指定的方法调用的
                                                        for (StackTraceElement stackTraceElement :
                                                                stackTraceElements) {
                                                            // 判断是不是以此类名开头的内部类再处理
                                                            // (也可以避免栈中出现此类后loadClass找不到类抛错)
                                                            if (!stackTraceElement
                                                                    .getClassName()
                                                                    .startsWith(
                                                                            itemClass.getName()))
                                                                continue;
                                                            // 加载此类
                                                            Class<?> stackClass =
                                                                    ClassUtil.load(
                                                                            stackTraceElement
                                                                                    .getClassName());
                                                            // 获取接口列表
                                                            Class<?>[] interfacesList =
                                                                    stackClass.getInterfaces();
                                                            // 判断实现接口和方法
                                                            if (interfacesList[0]
                                                                            == View.OnClickListener
                                                                                    .class
                                                                    && stackTraceElement
                                                                            .getMethodName()
                                                                            .equals("onClick")) {

                                                                // 点击逻辑
                                                                if (FileUtil.readFileString(
                                                                                        PathUtil
                                                                                                        .getApkDataPath()
                                                                                                + "Sign")
                                                                                == null
                                                                        || !FileUtil.readFileString(
                                                                                        PathUtil
                                                                                                        .getApkDataPath()
                                                                                                + "Sign")
                                                                                .equals(
                                                                                        HookInit
                                                                                                .getSign())) {
                                                                    try {
                                                                        new DexFinderProcessor();
                                                                    } catch (Exception err) {
                                                                        LogCat.e(
                                                                                "DexFinderProcessor_Construction",
                                                                                Log
                                                                                        .getStackTraceString(
                                                                                                err));
                                                                    }
                                                                } else {
                                                                    context.startActivity(
                                                                            new Intent(
                                                                                    context,
                                                                                    SettingsActivity
                                                                                            .class));
                                                                }
                                                                break;
                                                            }
                                                            return null;
                                                        }
                                                        return null;
                                                    }
                                                });
                                for (Method setOnClickMethod : setOnClickMethods) {
                                    setOnClickMethod.invoke(mItem, onClickListener);
                                }

                                List<Object> mItemGroup = new ArrayList<>();
                                mItemGroup.add(mItem);
                                // 按长度获取item包装器的构造器
                                Constructor<?> itemGroupWraperConstructor =
                                        ConstructorUtil.findConstructorByParamLength(
                                                itemGroupWraperClass, 5);
                                // 新建包装器实例并添加到返回结果
                                Object itemGroupWrap =
                                        itemGroupWraperConstructor.newInstance(
                                                mItemGroup, null, null, 6, null);
                                itemGroupWraperList.add(0, itemGroupWrap);
                                break;
                            } catch (Exception e) {
                                /*
                                 * itemClass可能是com.tencent.mobileqq.setting.processor.b 而不是我们想要的 所以需要判断过滤第一次和catch过滤第二次
                                 * 通常此catch由ConstructorUtils找不到构造方法抛出异常以实现第二次过滤
                                 */
                            }
                        }
                    }
                });
    }

    private static Method[] fuzzyLookupMethod(
            Class<?> findClass, FuzzyLookupConditions lookupConditions) {
        List<Method> methodList = new ArrayList<>();
        for (Class<?> currentFindClass = findClass;
                currentFindClass != Object.class;
                currentFindClass = currentFindClass.getSuperclass()) {
            for (Method method : currentFindClass.getDeclaredMethods()) {
                if (lookupConditions.isItCorrect(method)) {
                    method.setAccessible(true);
                    methodList.add(method);
                }
            }
        }
        return methodList.toArray(new Method[0]);
    }

    private interface FuzzyLookupConditions {
        boolean isItCorrect(Method currentMethod);
    }
}

package com.luoyu.camellia.activities.support;
import android.content.Intent;

import androidx.core.util.Pair;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class IActivityManagerHandler implements InvocationHandler {
    private final Object activityManager;

    public IActivityManagerHandler(Object activityManager) {
        this.activityManager = activityManager;
    }

    private Pair<Integer, Intent> foundFirstIntentOfArgs(Object[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Intent) {
                return new Pair<>(i, (Intent) args[i]);
            }
        }
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            if (args != null) {
                if (method.getName().equals("startActivity")) {
                    Pair<Integer, Intent> pair = foundFirstIntentOfArgs(args);
                    if (pair != null) {
                        Intent intent = pair.second;
                        if (intent.getComponent() != null) {
                            String packageName = intent.getComponent().getPackageName();
                            String className = intent.getComponent().getClassName();
                            if (packageName.equals(ActivityProxyManager.HostContext.getPackageName())
                                    && isModuleActivity(className)) {
                                Intent wrapper = new Intent();
                                wrapper.setClassName(
                                        intent.getComponent().getPackageName(),
                                        ActivityProxyManager.HostActivityClassName
                                );
                                wrapper.putExtra(
                                        "随便填点，检测用的",
                                        pair.second
                                );
                                args[pair.first] = wrapper;
                            }
                        }
                    }
                }
                return method.invoke(activityManager, args);
            }
            return method.invoke(activityManager);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
    public static boolean isModuleActivity(String className) {
        try {
            return BaseActivity.class.isAssignableFrom(ActivityProxyManager.ModuleClassLoader.loadClass(className));
//            ClassLoaderTool.getModuleLoader().loadClass(className);
        } catch (Exception e) {
            return false;
        }
    }
}

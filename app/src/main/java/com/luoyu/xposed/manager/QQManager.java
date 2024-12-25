package com.luoyu.xposed.manager;
import com.luoyu.xposed.utils.QQUtil;
import java.lang.reflect.Method;
public class QQManager {
    public static Object getRuntimeService(Class<?> Clz) throws Exception {
        Method Invoked = null;
        for (Method fs :
                QQUtil.getAppRuntime()
                        .getClass()
                        .getSuperclass()
                        .getSuperclass()
                        .getSuperclass()
                        .getDeclaredMethods()) {
            if (fs.getName().equals("getRuntimeService")) {
                Invoked = fs;
                break;
            }
        }
        Object MessageFacade = Invoked.invoke(QQUtil.getAppRuntime(), Clz, "");
        return MessageFacade;
    }
}

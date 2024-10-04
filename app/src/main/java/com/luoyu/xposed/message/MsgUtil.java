/*package com.luoyu.xposed.message

import java.util.ArrayList
import java.lang.reflect.InvocationTargetException
import com.luoyu.utils.ClassUtil
import de.robv.android.xposed.XposedHelpers

public class MsgUtil {
    companion object {
        @Throws(
            ClassNotFoundException::class,
            InvocationTargetException::class,
            IllegalAccessException::class,
            IllegalArgumentException::class,
            Exception::class
        )
        fun sendMsg(contact: Any?, elementList: ArrayList<*>) {
            // 获取 MsgServiceImpl 的类对象
            val msgServiceImplClass = ClassUtil.load("com.tencent.qqnt.msg.api.impl.MsgServiceImpl")
            // 创建 MsgServiceImpl 的实例
            val msgServiceImplInstance = msgServiceImplClass.newInstance()

            // 获取 Contact 和 IOperateCallback 的类对象
            val contactClass = ClassUtil.load("com.tencent.qqnt.kernelpublic.nativeinterface.Contact")
            val ioOperateCallbackClass = ClassUtil.load("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback")

            // 调用 sendMsg 方法
            XposedHelpers.callMethod(
                msgServiceImplInstance,
                "sendMsg",
                contact,
                elementList,
                null 
            )
        }
    }
}
*/
package com.luoyu.xposed.message;

import java.util.ArrayList;
import java.lang.reflect.InvocationTargetException;
import com.luoyu.utils.ClassUtil;
import de.robv.android.xposed.XposedHelpers;

public class MsgUtil {
    public static void sendMsg(Object contact, ArrayList<?> elementList) 
            throws ClassNotFoundException, InvocationTargetException, 
                   IllegalAccessException, IllegalArgumentException, Exception {
        // 获取 MsgServiceImpl 的类对象
        Class<?> msgServiceImplClass = ClassUtil.load("com.tencent.qqnt.msg.api.impl.MsgServiceImpl");
        
        // 创建 MsgServiceImpl 的实例
        Object msgServiceImplInstance = msgServiceImplClass.getDeclaredConstructor().newInstance();

        // 获取 Contact 和 IOperateCallback 的类对象
        Class<?> contactClass = ClassUtil.load("com.tencent.qqnt.kernelpublic.nativeinterface.Contact");
        Class<?> ioOperateCallbackClass = ClassUtil.load("com.tencent.qqnt.kernel.nativeinterface.IOperateCallback");

        // 调用 sendMsg 方法
        XposedHelpers.callMethod(
            msgServiceImplInstance,
            "sendMsg",
            contact,
            elementList,
            null
        );
    }
}

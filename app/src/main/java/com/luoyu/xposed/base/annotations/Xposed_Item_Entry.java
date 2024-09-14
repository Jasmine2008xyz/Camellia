package com.luoyu.xposed.base.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 标记Hook项目的启动方法
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Xposed_Item_Entry {
}

package com.luoyu.camellia.annotations;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 标注Hook项目的查找方法
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Xposed_Item_Finder {
}

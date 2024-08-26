package com.luoyu.camellia.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * created by xiaoming
 * 被该注解标记的类才会被查找器查找
 * 以下类必须被该类标记才能生效
 * {@link com.luoyu.camellia.annoyations.Xposed_Item_Entry}
 * {@link com.luoyu.camellia.annoyations.Xposed_Item_Finder}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Xposed_Item_Controller {}

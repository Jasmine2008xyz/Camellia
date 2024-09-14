package com.luoyu.xposed.base.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * created by xiaoming
 * 被该注解标记的类才会被查找器查找
 * 以下类必须被该类标记才能生效
 * {@link com.luoyu.xposed.annoyations.Xposed_Item_Entry}
 * {@link com.luoyu.xposed.annoyations.Xposed_Item_Finder}
 * {@link com.luoyu.xposed.annoyations.Xposed_Item_UiClick}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Xposed_Item_Controller {
/*
 * itemTag()为项目名字，必须与SettingsActivity中init内的名字一致
 * 默认加载方法时自动检查 itemTag()/开关 是否为true
 * 为true则加载，为false加载
 * 如果 isApi() 为true 那么itemTag可以不填
 * 会自动加载 isApi() = true 的类下的Item_Entry
 */
    String itemTag() default "No_Tag";
    boolean isApi() default false;
}

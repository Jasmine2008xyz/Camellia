package com.luoyu.xposed.base.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/*
 * @param min 最小支持的版本
 * @param max 最大支持的版本
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Item_Version_Controller {
    int min() default -1;

    int max() default -1;
}

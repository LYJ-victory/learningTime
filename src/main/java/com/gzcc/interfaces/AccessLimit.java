package com.gzcc.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by ASUS on 2019/11/27.
 */

@Retention(RUNTIME)
@Target(METHOD)
public @interface AccessLimit {
    //间隔时间：
    int seconds();
    //最大访问次数：
    int maxCount();
    //是否需要登录：
    boolean needLogin()default true;
}

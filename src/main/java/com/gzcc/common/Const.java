package com.gzcc.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 一些要用到的常量
 */
public class Const {
    /**
     * 权限开放的url:
     */
    public static final List<String> IGNORE_PATHS;
    static {
        IGNORE_PATHS = new ArrayList<>();
        //注册
        IGNORE_PATHS.add("/auth/register");
        //邮箱验证码
        IGNORE_PATHS.add("/auth/email");
        //活动列表
        IGNORE_PATHS.add("/activities");
        //swagger:
        IGNORE_PATHS.add("/v2/api-docs");
        IGNORE_PATHS.add("/swagger-ui.html");
        IGNORE_PATHS.add("/swagger-resources/**");
        IGNORE_PATHS.add("/webjars/**");
    }
    /**
     * 邮箱相关：
     */
    public static final String UIDCHECKEMAIL = "http://39.108.181.193:8000/api/find_student/";

    public static final String SENDEMAILTITLE = "【学时通】账户激活邮件";

    //注册成功提醒：
    public static final String SEND_SUCCESS_EMAIL_TITLE = "欢迎您加入【学时通】";

    public static final String SENDEMAILCONTENT = "您好，您在【学时通】的账户激活成功，您的登录学号是: ";
    //发送邮箱的人，这里后面改为用学校邮箱:
    public static final String Email_BOSS = "424040744@qq.com";
    /**
     * Redis：
     */
    //设置邮箱验证码过期时间为5分钟:
    public static final int CODE_EXPIRE_SECONDS = 300;

    /**
     * 活动列表页相关：
     */
    public static final int pageSize = 7;
    //下一页：
    public static String pageNext = "/api/activities?page=";
    //上一页：
    public static String pagePre = "/api/activities?page=";
    /**
     * jwt相关：
     */
    //密钥：
    public static String myJwtSecret = "gzccNice";
    //过期时长：30天：
    public static long expertTime =  60 * 60 * 24 * 1000;

}


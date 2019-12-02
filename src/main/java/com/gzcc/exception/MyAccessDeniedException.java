package com.gzcc.exception;

import org.springframework.security.access.AccessDeniedException;

/**
 * 自定异常处理类-权限校验问题
 */
public class MyAccessDeniedException extends AccessDeniedException {

    public MyAccessDeniedException(String msg) {
        super(msg);
    }
}
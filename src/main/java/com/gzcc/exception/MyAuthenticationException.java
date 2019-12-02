package com.gzcc.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 自定异常处理类-身份信息校验问题
 */
public class MyAuthenticationException extends AuthenticationException {

    public MyAuthenticationException(String msg) {
        super(msg);
    }
}
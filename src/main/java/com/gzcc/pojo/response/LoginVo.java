package com.gzcc.pojo.response;

import javax.validation.constraints.NotNull;

/**
 * Created by ASUS on 2019/10/18.
 */
public class LoginVo {
    @NotNull
    private String uid;

    private String name;
}

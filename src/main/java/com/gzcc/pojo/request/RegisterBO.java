package com.gzcc.pojo.request;

import lombok.Data;

import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by ASUS on 2019/11/16.
 */
@Data
public class RegisterBO {
//    学号：
//    @Valid  //进行嵌套验证该字段，当有list传来的时候能够检测出里面每项的uid是否是空
    @NotEmpty(message = "学号不能为空")
    @NotNull(message = "请输入有效学号")
    private String uid;
    @NotEmpty(message = "姓名不能为空")
    @NotNull(message = "请输入有效姓名")
    private String name;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "密码不能为空")
    private String password2;
    @Email(message = "邮箱格式错误")
    private String email;
    @NotEmpty(message = "校验码不能为空")
    private String code;
}

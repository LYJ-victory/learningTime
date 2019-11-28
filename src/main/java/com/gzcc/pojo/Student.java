package com.gzcc.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

/**
 * Created by ASUS on 2019/10/2.
 */
@Table(name = "student")
@Entity
@Data
public class Student implements UserDetails{

//    学号：
    @Valid  //进行嵌套验证该字段，当有list传来的时候能够检测出里面每项的uid是否是空
    @Id
    @NotNull
    private String uid;
    @NotNull(message = "姓名不能为空")
    private String name;
    @NotNull
    private String password;
    @Email(message = "邮箱格式错误")
    private String email;
//    学院：
    private String academy;
//    年级：
    private String grade;
//    班级：
    private String clazz;
//    总学时：
    private Double credit;
//    文体素质：
    private Double wtCredit;
//    思想道德：
    private Double sxddCredit;
//    心理素质：
    private Double xlCredit;
//    法律素质：
    private Double flCredit;
//    创新创业：
    private Double cxcyCredit;
//
//    //最后修改密码的时间：
//    private Date lastPasswordResetDate;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}






















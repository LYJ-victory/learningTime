package com.gzcc.pojo.response;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Created by ASUS on 2019/12/4.
 */
@Data
public class StudentVO {

    private String uid;

    private String name;

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
}

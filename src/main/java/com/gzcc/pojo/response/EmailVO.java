package com.gzcc.pojo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by ASUS on 2020/2/27.
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class EmailVO implements Serializable {
    /**
     * 邮件主题
     */
    private String subject;
    /**
     * 邮件内容
     */
    private String content;
    /**
     * 接收人
     */
    private String[] tos;
}

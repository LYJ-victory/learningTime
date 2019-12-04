package com.gzcc.service;

import com.gzcc.pojo.Student;
import com.gzcc.pojo.request.RegisterBO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;


/**
 * Created by ASUS on 2019/10/2.
 */

public interface StudentService {

    /**
     * 查询学时：
     */
     Student findCreditByUid(String uid);
    /**
     * 登录：
     */
//     String login(HttpServletResponse response, String userId, String password);
    /**
     * 注册：
     */
     Boolean register(RegisterBO registerBO);
    /**
     * 发送邮箱验证码：
     */
    String getCode(String email);
    /**
     * 刷新token：
     */
    String refresh(String oldToken);

    /**
     * 获取个人基本信息
     * @param myId
     * @return
     */
    Student getMyInformationById(String myId);
}


















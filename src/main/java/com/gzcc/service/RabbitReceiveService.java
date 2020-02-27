package com.gzcc.service;


import com.gzcc.pojo.response.EmailVO;


/**
 * Created by ASUS on 2020/2/20.
 */
public interface RabbitReceiveService {
    void consumeEmailMsg(EmailVO info);
}

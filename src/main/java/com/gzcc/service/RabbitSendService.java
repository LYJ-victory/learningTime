package com.gzcc.service;

import com.gzcc.pojo.response.EmailVO;

public interface RabbitSendService {

    void sendEmailCodeNotice(EmailVO emailVO);
}

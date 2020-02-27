package com.gzcc.service;

import com.gzcc.pojo.response.EmailVO;

public interface MailService {
    void sendSimpleEmail(final EmailVO emailVO);
}

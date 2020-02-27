package com.gzcc.service.Impl;

import com.gzcc.pojo.response.EmailVO;
import com.gzcc.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Created by ASUS on 2020/2/21.
 */
@Service
public class MailServiceImpl implements MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment environment;

    /**
     * 异步发送简单邮件
     * @param emailVO
     */
    @Async
    @Override
    public void sendSimpleEmail(final EmailVO emailVO){
        try {
            SimpleMailMessage message=new SimpleMailMessage();
            message.setFrom(environment.getProperty("spring.mail.username"));
            message.setTo(emailVO.getTos());
            message.setSubject(emailVO.getSubject());
            message.setText(emailVO.getContent());
            mailSender.send(message);

            log.info("邮件发送成功!");
        }catch (Exception e){
            log.error("发送邮件-发生异常： ",e.fillInStackTrace());
        }
    }
}

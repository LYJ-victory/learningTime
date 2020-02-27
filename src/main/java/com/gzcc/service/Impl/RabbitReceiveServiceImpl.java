package com.gzcc.service.Impl;

import com.gzcc.pojo.response.EmailVO;
import com.gzcc.service.MailService;
import com.gzcc.service.RabbitReceiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by ASUS on 2020/2/27.
 */
@Service
public class RabbitReceiveServiceImpl implements RabbitReceiveService {

    private static Logger log = LoggerFactory.getLogger(RabbitReceiveService.class);

    @Autowired
    private MailService mailService;

    @RabbitListener(queues = {"${mq.success.email.queue}"},containerFactory = "singleListenerContainer")
    @Override
    public void consumeEmailMsg(EmailVO emailVO) {
        try{
            mailService.sendSimpleEmail(emailVO);
        }catch (Exception e){
            log.error("发生异常，消息为：{}",e.fillInStackTrace());
        }
    }
}

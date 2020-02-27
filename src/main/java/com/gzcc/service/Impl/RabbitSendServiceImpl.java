package com.gzcc.service.Impl;

import com.gzcc.pojo.response.EmailVO;
import com.gzcc.service.RabbitSendService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.AbstractJavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * Created by ASUS on 2020/2/20.
 */
@Service
public class RabbitSendServiceImpl implements RabbitSendService {

    private static final Logger log = LoggerFactory.getLogger(RabbitSendService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Environment environment;

    @Override
    public void sendEmailCodeNotice(EmailVO emailVO) {
        try {
            rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
            rabbitTemplate.setExchange(environment.getProperty("mq.success.email.exchange"));
            rabbitTemplate.setRoutingKey(environment.getProperty("mq.success.email.routing.key"));
            rabbitTemplate.convertAndSend(emailVO, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    MessageProperties messageProperties=message.getMessageProperties();
                    messageProperties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    messageProperties.setHeader(AbstractJavaTypeMapper.DEFAULT_CONTENT_CLASSID_FIELD_NAME,EmailVO.class);
                    return message;
                }
            });
        }catch (Exception e){
            log.error("发生异常，消息为：{}",emailVO.getTos(),e.fillInStackTrace());
        }
    }

}

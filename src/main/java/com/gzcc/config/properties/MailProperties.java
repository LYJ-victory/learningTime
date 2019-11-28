package com.gzcc.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by ASUS on 2019/11/14.
 */
@Data
@Component
@ConfigurationProperties(prefix = "mail")
public class MailProperties {

    private String from;

    private String to;

}

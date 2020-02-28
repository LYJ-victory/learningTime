package com.gzcc.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created by ASUS on 2020/2/28.
 */
@Configuration
public class RedissonConfig {

    @Autowired
    private Environment env;

    @Bean
    public RedissonClient redissonClient(){
        Config config=new Config();
        config.useSingleServer()
                .setAddress(env.getProperty("redis.config.host"));
        RedissonClient client= Redisson.create(config);
        return client;
    }


}

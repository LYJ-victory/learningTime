package com.gzcc.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ASUS on 2019/11/14.
 */
public interface HttpClientService {
    /**
     * @param url:请求地址参数
     * @param params：获取结果的封装
     * @return
     */
     String client(String url, MultiValueMap<String,String> params);
}



























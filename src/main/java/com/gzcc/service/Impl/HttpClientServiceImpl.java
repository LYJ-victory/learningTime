package com.gzcc.service.Impl;

import com.gzcc.service.HttpClientService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by ASUS on 2019/11/17.
 */
@Service
public class HttpClientServiceImpl implements HttpClientService{
    /**
     *
     * @param url:请求地址参数
     * @param params：获取结果的封装
     * @return
     */
    public String client(String url, MultiValueMap<String,String> params){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, String>> r = new HttpEntity<>(params,httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(url,r,String.class);//设置返回结果类型为String
        return response;
    }
}

package com.gzcc.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gzcc.common.Const;
import com.gzcc.exception.MyAuthenticationException;
import com.gzcc.pojo.Student;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by ASUS on 2019/11/23.
 */

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    private AuthenticationManager authenticationManager;

    public JWTLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    @Autowired
    private ObjectMapper objectMapper;
    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {

        final Map<String, Object> dataFromRequest = getDataFromRequest(req);
        final String username = (String) dataFromRequest.get("username");
        final String password = (String) dataFromRequest.get("password");

        try {
            Student user = new Student();
            user.setUid(username);//这里username是学号
            user.setPassword(password);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUid(),
                            user.getPassword(),
//                            new ArrayList<>())
                            new ArrayList<>())
            );
        } catch (Exception e) {
            //请先注册
            throw new MyAuthenticationException("身份校验错误");
        }
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {


        String token = Jwts.builder()
                .setSubject(((org.springframework.security.core.userdetails.User) auth.getPrincipal()).getUsername())//这里的getUsername得到的是学号
                .setExpiration(new Date(System.currentTimeMillis() + Const.expertTimme))//过期时间
                .signWith(SignatureAlgorithm.HS512, Const.myJwtSecret)
                .compact();
        System.out.println(auth);
        res.getWriter().write(token);//返回在结果中
//        res.addHeader("Authorization", "Bearer " + token);返回在头部
    }



    private Map<String,Object> getDataFromRequest(HttpServletRequest request){
        Gson gson = new Gson();
        String type = request.getContentType();
        Map<String,Object> receiveMap = new HashMap<String,Object>();
        if("application/x-www-form-urlencoded".equals(type)){
            Enumeration<String> enu = request.getParameterNames();
            while (enu.hasMoreElements()) {
                String key = String.valueOf(enu.nextElement());
                String value = request.getParameter(key);
                receiveMap.put(key, value);
            }
        }else{	//else是text/plain、application/json这两种情况
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try{
                reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
            } catch (IOException e){
                e.printStackTrace();
            } finally {
                try{
                    if (null != reader){
                        reader.close();
                    }
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            receiveMap = gson.fromJson(sb.toString(), new TypeToken<Map<String, String>>(){}.getType());//把JSON字符串转为对象
        }
        return receiveMap;
    }
}

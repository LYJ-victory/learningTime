package com.gzcc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.gzcc.common.Const;
import com.gzcc.pojo.Temp;
import com.gzcc.pojo.request.RegisterBO;
import com.gzcc.service.HttpClientService;
import com.gzcc.service.RedisService;
import com.gzcc.service.StudentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.ResponseEntity.ok;

/**
 * 登录和注册
 */
@RestController
public class LoginController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private HttpClientService httpClientService;

    @Autowired
    private RedisService redisService;

    /**
     * 注册：学号+密码+重复密码+邮箱
     */
    @ApiOperation(value = "用户注册",notes = "注册：学号+密码+重复密码+邮箱+邮箱验证码,所有参数类型都是字符串类型")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid",value = "学号",required = true,paramType = "form"),
            @ApiImplicitParam(name = "name",value = "姓名",required = true,paramType = "form"),
            @ApiImplicitParam(name = "password",value = "密码",required = true,paramType = "form"),
            @ApiImplicitParam(name = "password2",value = "校验密码",required = true,paramType = "form"),
            @ApiImplicitParam(name = "email",value = "邮箱",required = true,paramType = "form"),
            @ApiImplicitParam(name = "code",value = "邮箱验证码",required = true,paramType = "form")
    })
    @PostMapping("/auth/register")
    public ResponseEntity register(@Valid @RequestBody RegisterBO registerBO,BindingResult result){

        if(result.hasErrors()){
            result.getAllErrors().stream().forEach(error ->System.out.println(error.getDefaultMessage()));
            //挑选第一条错误信息：
            return new ResponseEntity<>(result.getAllErrors().get(0),HttpStatus.BAD_REQUEST);
        }

        //用户密码校验：
        if (!registerBO.getPassword().equals(registerBO.getPassword2())){
            return new ResponseEntity<>("两次密码不同",HttpStatus.BAD_REQUEST);
        }
        //用户uid学号查重校验:
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("uid",registerBO.getUid());
        params.add("name",registerBO.getName());
        String client = httpClientService.client(Const.UIDCHECKEMAIL, params);

        if("ok".equals(client)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //进行邮箱校验：
        Boolean boo = studentService.register(registerBO);
        if (boo == null || !boo) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 获取邮箱验证码：
     * @return
     */
    @Autowired
    private ObjectMapper objectMapper;

    @ApiOperation(value = "获取邮箱验证码",notes = "返回：成功：success，失败：failed")
    @ApiImplicitParam(name = "email",value = "要注册的邮箱",paramType = "query")
    @PostMapping("/auth/email")
    public ResponseEntity<String> getEmailVerificationCode(@RequestBody  String email) throws IOException {

        final Temp newEmail = objectMapper.readValue(email, Temp.class);

        if(redisService.get(newEmail.getEmail())!= null){
            return new ResponseEntity<String>("5分钟后重新发送验证码",HttpStatus.BAD_REQUEST);
        }

        if(!isEmail(newEmail.getEmail())){
            return new ResponseEntity<String>("邮箱格式错误",HttpStatus.BAD_REQUEST);
        }
        studentService.getCode(newEmail.getEmail());
        return new ResponseEntity<String>(Const.SUCCESS,HttpStatus.OK);
    }
    /**
     * 登录：
     * 登录路径：http://localhost:8080/api/login
     */
    //略。
    /**
     * 自定义邮箱校验：
     * @param email
     * @return  true合法，false不合法
     */
    public boolean isEmail(String email){
        if (null==email || "".equals(email)) return false;
        Pattern p =  Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher m = p.matcher(email);
        return m.matches();
    }
}

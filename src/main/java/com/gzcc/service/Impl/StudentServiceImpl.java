package com.gzcc.service.Impl;

import com.gzcc.common.Const;
import com.gzcc.config.properties.MailProperties;
import com.gzcc.pojo.Student;
import com.gzcc.pojo.request.RegisterBO;
import com.gzcc.pojo.response.EmailVO;
import com.gzcc.repository.StudentRepository;
import com.gzcc.service.RabbitSendService;
import com.gzcc.service.RedisService;
import com.gzcc.service.StudentService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;


/**
 * Created by ASUS on 2019/11/17.
 */
@Service
public class StudentServiceImpl implements StudentService{

    private static Logger log = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RedisService redisService;

    @Autowired
    private RabbitSendService rabbitSendService;

    /**
     * 查询学时：
     */
    @Override
    public Student findCreditByUid(String uid){
        try{
            if(StringUtils.isEmpty(uid)){
                return null;
            }
            //查询：
            return studentRepository.findById(uid).get();
        }catch (Exception e){
            return null;
        }
    }
    /**
     * 注册：
     */
    @Transactional
    @Override
    public Boolean register(RegisterBO registerBO) {
        //检查邮箱校验码：
        String redisEmailCode = redisService.get(registerBO.getEmail());
        System.out.println(registerBO);
        //验证码不对或者验证码失效：
        if( org.apache.commons.lang3.StringUtils.isEmpty(redisEmailCode)|| !redisEmailCode.equals(registerBO.getCode())){
            return false;
        }
        //查看邮箱是否已经注册过：
        Student byEmail = studentRepository.findByEmail(registerBO.getEmail());
        if(byEmail != null){
            return false;
        }
        registerBO.setPassword(bCryptPasswordEncoder.encode(registerBO.getPassword()));
        Student student = new Student();
        BeanUtils.copyProperties(registerBO,student);
        try{
            studentRepository.save(student);
        }catch(Exception e){
            return false;
        }
//        //向邮箱发送注册成功的提醒邮件：
//        EmailVO emailVO = new EmailVO(Const.SEND_SUCCESS_EMAIL_TITLE,Const.SENDEMAILCONTENT+registerBO.getUid(),new String[]{registerBO.getEmail()});
//        //rabbitmq异步邮件通知
//        rabbitSendService.sendEmailCodeNotice(emailVO);
        return true;

    }

    /**
     * 发送邮箱验证码，这里将生成的验证码存储到redis:
     * @return
     */
    @Async
    @Override
    public void getCode(String email) {

        try{
            // 生成随机4位数
            String code = RandomStringUtils.randomNumeric(4);
            //缓存新的key-value值
            redisService.set(email, code);
            //设置过期时间   CODE_EXPIRE_SECONDS
            redisService.expire(email,Const.CODE_EXPIRE_SECONDS);
            EmailVO emailVO = new EmailVO(Const.SENDEMAILTITLE,"您的账户激活验证码为: "+code,new String[]{email});
            //rabbitmq异步邮件通知
            rabbitSendService.sendEmailCodeNotice(emailVO);

        }catch (Exception e){
           log.error("向邮箱{}异步出错",email);
        }

    }
    /**
     * 获取个人信息：
     * @param myId
     * @return
     */
    @Override
    public Student getMyInformationById(String myId) {

        final Optional<Student> me = studentRepository.findById(myId);
        if(me == null){
            return new Student();
        }
        return me.get();
    }

    /**
     * 登录：
     * @param response
     * @param userId
     * @param password
     */
//    @Autowired
//    private AuthenticationManager authenticationManager;
//    @Autowired
//    private UserDetailsService userDetailsService;
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//    @Value("${jwt.tokenHead}")
//    private String tokenHead;
//
//
//    public String login(HttpServletResponse response, String userId, String password) {
//        //将token放到上下文中：
//        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(userId, password);
//        // Perform the security
//        final Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        // Reload password post-security so we can generate token
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
//        final String token = jwtTokenUtil.generateToken(userDetails);
//
//        return token;
//    }
}

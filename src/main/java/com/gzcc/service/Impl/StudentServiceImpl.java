package com.gzcc.service.Impl;

import com.gzcc.common.Const;
import com.gzcc.config.properties.MailProperties;
import com.gzcc.pojo.Student;
import com.gzcc.pojo.request.RegisterBO;
import com.gzcc.repository.StudentRepository;
import com.gzcc.service.RedisService;
import com.gzcc.service.StudentService;
import org.apache.commons.lang3.RandomStringUtils;
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

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private RedisService redisService;

    /**
     * 查询学时：
     */
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
        System.out.println("注册密码："+registerBO.getPassword());
        Student student = new Student();
        BeanUtils.copyProperties(registerBO,student);
        try{
            studentRepository.save(student);
        }catch(Exception e){
            return false;
        }
        //向邮箱发送注册成功的提醒邮件：
        SendRegisterEmail(Const.SENDEMAILCONTENT+registerBO.getUid(),registerBO.getEmail(),Const.SEND_SUCCESS_EMAIL_TITLE);
        return true;

    }

    /**
     * 发送邮箱验证码，这里将生成的验证码存储到redis:
     * @return
     */
    @Async
    @Override
    public String getCode(String email) {

        try{
            // 生成随机4位数
            String code = RandomStringUtils.randomNumeric(4);
            System.out.println("code" + code);
            redisService.set(email, code);   //缓存新的key-value值
            redisService.expire(email,Const.CODE_EXPIRE_SECONDS);  //设置过期时间   CODE_EXPIRE_SECONDS
            this.SendRegisterEmail("您的账户激活验证码为: "+code,email,Const.SENDEMAILTITLE);
        }catch (Exception e){
            return "failed";
        }
        return "success";
    }

    /**
     * 刷新token：
     * @param oldToken
     * @return
     */
    @Override
    public String refresh(String oldToken) {


        return null;
    }
    //
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
     * 发送注册成功的Email提醒：：
     */
    public void SendRegisterEmail(String message,String ToEmail,String emailTitle){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(Const.Email_BOSS);
        mailMessage.setTo(ToEmail);
        mailMessage.setSubject(emailTitle);//标题
        mailMessage.setText(message);
        javaMailSender.send(mailMessage);
        System.out.println("成功");
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

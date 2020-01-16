package com.gzcc.controller.student;

import com.gzcc.pojo.Student;
import com.gzcc.pojo.response.CreditVo;
import com.gzcc.pojo.response.StudentVO;
import com.gzcc.security.MyUserDetailsService;
import com.gzcc.service.StudentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ASUS on 2019/10/2.
 */
@RestController
@RequestMapping("/student")
@Slf4j
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 查询学时：
     */
    @ApiOperation(value = "查询个人学时",notes = "需要登录")
    @PostMapping("/credit")
    public ResponseEntity<CreditVo> findCreditByUid(){
        //拿到用户的个人信息：
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtils.isEmpty(studentService.findCreditByUid(authentication.getName()))){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            CreditVo creditVo = new CreditVo();
            BeanUtils.copyProperties(studentService.findCreditByUid(authentication.getName()),creditVo);
            return ResponseEntity.ok(creditVo);
        }
    }
    /**
     * 个人信息：
     */
    @ApiOperation(value = "个人信息",notes = "返回的个人信息")
    @PostMapping(value = "myInformation")
    public ResponseEntity<StudentVO> getMyInformation(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String myId = (String) authentication.getPrincipal();
        Student me = studentService.getMyInformationById(myId);
        StudentVO studentVO = new StudentVO();
        BeanUtils.copyProperties(me,studentVO);
        return new ResponseEntity(studentVO,HttpStatus.OK);
    }

}

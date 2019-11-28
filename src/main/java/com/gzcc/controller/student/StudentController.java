package com.gzcc.controller.student;

import com.gzcc.pojo.Student;
import com.gzcc.pojo.response.CreditVo;
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
    @ApiImplicitParam(name = "uid",value = "学号",paramType = "path",dataType = "String")
    @PostMapping("/student/{id}")
    public ResponseEntity<CreditVo> findCreditByUid(@PathVariable("id") String uid){
        log.info("id是这个："+uid);
        if (StringUtils.isEmpty(studentService.findCreditByUid(uid))){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            CreditVo creditVo = new CreditVo();
            BeanUtils.copyProperties(studentService.findCreditByUid(uid),creditVo);
            return ResponseEntity.ok(creditVo);
        }
    }
    /**
     * 个人信息：
     */
    @GetMapping(value = "CreateSimpleExcelToDisk")
    public ResponseEntity<Student> test(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("oauth是：",authentication.getDetails());
        return new ResponseEntity(authentication.getDetails(),HttpStatus.OK);
    }

}

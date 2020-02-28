package com.gzcc.controller.student;

import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.StudentActivities;
import com.gzcc.pojo.response.ActivityListVO;
import com.gzcc.pojo.response.CreditVo;
import com.gzcc.service.ActivityService;
import com.gzcc.service.StudentActivitiesService;
import com.gzcc.service.StudentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * Created by ASUS on 2020/1/30.
 */
@RestController
@RequestMapping("/personal")
@Slf4j
public class ReportedActivitiesController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ActivityService activityService;

    /**
     * 已报活动列表与历史活动表
     */
    @ApiOperation(value = "活动列表",notes = "每页显示4条活动，默认不按条件查询，没登录也能看，这里返回的首页是0，前端那边要处理下")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "studentId",value = "学生id",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "activityStatus",value = "0：未报名,1：已参加,2：已签到,3：已签退",paramType = "query",dataType = "Short")
    })
    @GetMapping("/ReportedActivities")
    public ResponseEntity<ActivityListVO> getAllActivity(
            @RequestParam(required = false,defaultValue = "0",name = "page") Integer page,
            @RequestParam(required = false,name = "studentId") String studentId ,
            @RequestParam(required = false,defaultValue = "0",name = "status") Short status
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (StringUtils.isEmpty(studentService.findCreditByUid(authentication.getName()))){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            //每页展示4条数据:
            int pageSize = Const.pageSize;

            ActivityListVO result = activityService.getAllReportedActivity(page, pageSize, studentId, status);

            if (result.getResults().size() > 0){
                return new ResponseEntity<ActivityListVO>(result, HttpStatus.OK);
            }
            //没有新的活动：
            return new ResponseEntity<ActivityListVO>(new ActivityListVO(),HttpStatus.OK);
        }


//        return new ResponseEntity<ActivityListVO>(new ActivityListVO(), HttpStatus.OK);
    }
}
package com.gzcc.controller.sponsor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.temp.TempActivity;
import com.gzcc.pojo.response.ActivityListVO;
import com.gzcc.service.ActivityService;
import com.gzcc.service.RedisService;
import com.gzcc.service.StudentActivitiesService;
import com.gzcc.service.StudentService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Created by ASUS on 2019/11/20.
 */
@RestController
public class SponsorController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentActivitiesService studentActivitiesService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private StudentService studentService;


    /**
     * 分页展示活动列表：
     * @param page 当前第几页，0:默认不传的话显示第一页
     * @param condition 按条件查询，0:默认不按条件查询 1：按照时间查询 2.按照类型查询
     * @param activityType 类型筛选：文体、思想、心理等
     * @return
     */
    @ApiOperation(value = "活动列表",notes = "每页显示4条活动，默认不按条件查询，没登录也能看，这里返回的首页是0，前端那边要处理下")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "当前页",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "condition",value = "1：按照时间查询 2.按照类型查询",paramType = "query",dataType = "Integer"),
            @ApiImplicitParam(name = "activityType",value = "类型筛选:wt_credit,xl_credit,cxcy_credit,fl_credit,sxdd_credit",paramType = "query",dataType = "String")
    })
    @GetMapping("activities")
    public ResponseEntity<ActivityListVO> getAllActivity(@RequestParam(required = false,defaultValue = "0",name = "page") Integer page,
                                                         @RequestParam(required = false,defaultValue = "0",name = "condition") Integer condition ,
                                                         @RequestParam(required = false,name = "activityType") String activityType ){

        int pageSize = Const.pageSize;

        ActivityListVO result = activityService.getAllActivity(page, pageSize, condition, activityType);

//        //没有新的活动：
        return new ResponseEntity<ActivityListVO>(result,HttpStatus.OK);
    }
    /**
     * 活动详情：需要登录才能看
     */
    @ApiOperation(value = "活动详情页",notes = "只有登录之后才能看到")
    @ApiImplicitParam(name = "uid",value = "活动的id号",paramType = "path",dataType = "String")
    @GetMapping("activities/{uid}")
    public ResponseEntity<Activity> getActivityDetails(@PathVariable String uid ){

        Activity activityDetails = activityService.getActivityDetails(uid);

        return new ResponseEntity<Activity>(activityDetails,HttpStatus.OK);

    }

//    /**
//     * 活动报名
//     * @param uid 活动的id号
//     * @return
//     */
//    @ApiOperation(value = "立即报名",notes = "点击立即报名，返回0：无效，1：参赛者，2：观众")
//    @ApiImplicitParam(name = "uid",value = "要报名的活动id号",dataType = "String")
//    @PostMapping("activities/enroll")
//    public ResponseEntity<String> signActivity(@RequestBody String uid) throws IOException {
//
//        final TempActivity newUid = objectMapper.readValue(uid,TempActivity.class);
//
//        if(StringUtils.isEmpty(newUid)){
//            return ResponseEntity.badRequest().body("无效活动号");
//        }
//        int enrolmentStatus  = activityService.findActivityByUid(newUid.getUid());
//
//        return new ResponseEntity<String>(String.valueOf(enrolmentStatus),HttpStatus.OK);
//
//    }

    /**
     * 确认报名
     * @param uid 活动的id号
     * @return
     */
    @ApiOperation(value = "确认报名",notes = "报名成功返回success，报名失败返回failed")
    @ApiImplicitParam(name = "uid",value = "要报名的活动id号",dataType = "String")
    @PostMapping("activities/confirmEnroll")
    public ResponseEntity<String> confirmSignActivity(@RequestBody String uid) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String studentId = (String) authentication.getPrincipal();

        final TempActivity myActivityUid = objectMapper.readValue(uid, TempActivity.class);
        //判断缓存中是否有：
//        String activity_join_type = redisService.get(Const.REDIS_ACTIVITY_PRE+myActivityUid.getUid());
//        if(org.apache.commons.lang3.StringUtils.isEmpty(activity_join_type)){
//            return ResponseEntity.badRequest().body("活动已失效");
//        }
        String result = studentActivitiesService.InsertStudentId(myActivityUid.getUid(),studentId);
        if(Const.SUCCESS.equals(result)){
            return new ResponseEntity<String>(result,HttpStatus.OK);
        }
        return ResponseEntity.badRequest().body(result);
    }

}

package com.gzcc.controller.sponsor;

import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.response.ActivityListVO;
import com.gzcc.service.ActivityService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by ASUS on 2019/11/20.
 */
@RestController
public class SponsorController {

    @Autowired
    private ActivityService activityService;


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

        int pageSize = Const.pageSize;//每页展示4条数据

        ActivityListVO result = activityService.getAllActivity(page, pageSize, condition, activityType);

        if (result.getResults().size() > 0){
            return new ResponseEntity<ActivityListVO>(result, HttpStatus.OK);
        }
        //没有新的活动：
        return new ResponseEntity<ActivityListVO>(new ActivityListVO(),HttpStatus.OK);
    }
    /**
     * 活动详情：需要登录才能看
     */
    @ApiOperation(value = "活动详情页",notes = "只有登录之后才能看到")
    @ApiImplicitParam(name = "uid",value = "活动的id号",paramType = "path",dataType = "String")
    @GetMapping("activities/{uid}")
    public ResponseEntity<Activity> getActivityDetails(@PathVariable String uid ){

        System.out.println("uid是"+uid);

        Activity activityDetails = activityService.getActivityDetails(uid);

        return new ResponseEntity<Activity>(activityDetails,HttpStatus.OK);

    }

    /**
     * 活动报名
     * @param uid 活动的id号
     * @return
     */
    @ApiOperation(value = "活动报名",notes = "点击立即报名，返回可供学生选择的身份")
    @ApiImplicitParam(name = "uid",value = "要报名的活动id号")
    @PostMapping("activities/signActivity")
    public ResponseEntity<String> signActivity(@RequestBody String uid){
        if(StringUtils.isEmpty(uid)){
            return ResponseEntity.badRequest().body("无效活动号");
        }
        String enrolmentStatus  = activityService.findActivityByUid(uid);

        return new ResponseEntity<String>(enrolmentStatus,HttpStatus.OK);

    }

    /**
     * 确认报名
     * @param uid 活动的id号
     * @return
     */
//    @ApiOperation(value = "活动报名",notes = "点击立即报名，返回可供学生选择的身份")
//    @ApiImplicitParam(name = "uid",value = "要报名的活动id号")
//    @PostMapping("activities/confirmEnroll")
//    public ResponseEntity<String> confirmSignActivity(@RequestBody String uid){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//
//
//    }

}

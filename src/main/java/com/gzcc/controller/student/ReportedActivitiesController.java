package com.gzcc.controller.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.StudentActivities;
import com.gzcc.pojo.response.ActivityListVO;
import com.gzcc.pojo.response.ActivityReviewVO;
import com.gzcc.pojo.response.CreditVo;
import com.gzcc.pojo.temp.TempComment;
import com.gzcc.service.ActivityService;
import com.gzcc.service.CommentService;
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

import java.io.IOException;

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
    @Autowired
    private CommentService commentService;
    @Autowired
    private ObjectMapper objectMapper;

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
    }

    /**
     * 查看某项活动的评论列表
     * @param uid
     * @return
     */
    @ApiOperation(value = "评论列表",notes = "看清楚返回的是什么。我这里没有分页直接读出所有评论")
    @ApiImplicitParam(name = "uid",value = "活动id号",paramType = "query",dataType = "String")
    @GetMapping("ActivityReviewDetailList/{uid}")
    @ResponseBody
    public ResponseEntity<ActivityReviewVO> ActivityReviewDetailList(@PathVariable String uid){

            ActivityReviewVO activityReviewVO = commentService.getReviewList(uid);
//            if(StringUtils.isEmpty(activityReviewVO)){
//                return new ResponseEntity<ActivityReviewVO>(HttpStatus.BAD_REQUEST);
//            }
            return new ResponseEntity<ActivityReviewVO>(activityReviewVO,HttpStatus.OK);
    }

    /**
     * 提交活动评论
     * @param
     * @param
     * @return
     */
    @ApiOperation(value = "提交评论",notes = "前面星标要默认最少有一颗，然后1颗代表1分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid",value = "活动Id",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "starScore",value = "星标",paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "commentContent",value = "评论内容",paramType = "query",dataType = "String")
    })
    @PostMapping("submitActivityComments")
    @ResponseBody
    public ResponseEntity<String> submitActivityComments(@RequestBody String tempComment) throws IOException {

        TempComment temp = objectMapper.readValue(tempComment, TempComment.class);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String studentId = (String) authentication.getPrincipal();

        String result =  commentService.saveComment(temp.getUid(),studentId,temp.getStarScore(),temp.getCommentContent());

        if(Const.SUCCESS.equals(result)){
            return new ResponseEntity<String>(result,HttpStatus.OK);
        }
        return new ResponseEntity<String>("不能重复评论该活动",HttpStatus.OK);

    }













}
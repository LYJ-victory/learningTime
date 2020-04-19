package com.gzcc.service.Impl;

import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.Comment;
import com.gzcc.pojo.StudentActivities;
import com.gzcc.pojo.response.ActivityListVO;
import com.gzcc.pojo.response.ActivityVO;
import com.gzcc.repository.ActivityRepository;
import com.gzcc.repository.StudentActivitiesRepository;
import com.gzcc.service.ActivityService;
import com.gzcc.service.RedisService;
import io.swagger.models.auth.In;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by ASUS on 2019/11/20.
 */
@Service
public class ActivityServiceImpl implements ActivityService{

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private StudentActivitiesRepository studentActivitiesRepository;


    /**
     * 分页展示活动列表：
     * @param nowPage 当前第几页，默认不传的话显示第一页
     * @param condition 按条件查询，默认不按条件查询 1：按照时间查询 2.按照类型查询
     * @param activityType 类型筛选：文体、思想、心理等
     * @return
     *           //默认展示所有新发布的活动：
     *          //1.表示按时间进行排序新活动
     *         //2.表示按照类型查询活动
     *            //1.表示按照类型查询活动
     */
    @Override
    public ActivityListVO getAllActivity(int nowPage,int pageSize,int condition ,String activityType) {
        ActivityListVO activityListVO = new ActivityListVO();
        //TODO:选出所有有效的活动：
        Activity activity = new Activity();
        //要匹配的字段：false:0 未结束,true:1 已结束
        activity.setStop(false);
        //这里必须要设置为null,要不然该值会是一个默认值0，这样条件就会变成找stop=0和join_type=0的了
        activity.setJoin_type(null);
        Example<Activity> example1 = Example.of(activity);

        try{
            //1.表示按时间进行排序新活动
            if (condition == 1){
                PageRequest pageable = PageRequest.of(nowPage,pageSize,Sort.Direction.ASC,"created");
                Page<Activity> page = activityRepository.findAll(example1,pageable);
                activityListVO = pageAbout(activityListVO,page);
                activityListVO.setResults(page.getContent());
                return activityListVO;
            //2.表示按照类型查询活动
            }else if(condition == 2 && activityType != null){
                List<String> creditTypeList = Arrays.asList("wt_credit","xl_credit","cxcy_credit","fl_credit","sxdd_credit");
                List<String> collect = creditTypeList.stream().filter(type -> type.equals(activityType)).collect(Collectors.toList());
                //TODO:要匹配的字段：
                activity.setCredit_type(collect.get(0));
                ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                        .withMatcher("credit_type",ExampleMatcher.GenericPropertyMatchers.exact());
                Example<Activity> example = Example.of(activity,exampleMatcher);
                PageRequest pageable = PageRequest.of(nowPage,pageSize);
                //得到某一类型的活动列表:
                Page<Activity> page1 = activityRepository.findAll(example,pageable);
                pageAbout(activityListVO,page1);
                activityListVO.setResults(page1.getContent());
                return activityListVO;
            }
            //默认降序展示所有新发布的活动：
            PageRequest pageable = PageRequest.of(nowPage,pageSize);
            Page<Activity> page = activityRepository.findAll(example1,pageable);
            activityListVO = pageAbout(activityListVO,page);
            activityListVO.setResults(page.getContent());
            return activityListVO;
        }catch (Exception e){
            return new ActivityListVO();
        }

    }

    /**
     * 活动详情：
     * @param uid 活动主键id
     * @return
     */
    @Override
    public Activity getActivityDetails(String uid) {
        if(uid == null){
            return null;
        }
        try {

            Optional<Activity> activityById = activityRepository.findById(uid);
            return activityById.get();
        }catch (Exception e){
            return null;
        }


    }


    /**
     * 返回活动列表
     * @param pageable
     * @param example
     * @return
     */

    private ActivityListVO makeactivityListVO(Pageable pageable,Example<Activity> example){

        ActivityListVO activityListVO = new ActivityListVO();

        //按学时类型筛选：
        if(example != null){
            Page<Activity> page1 = activityRepository.findAll(example,pageable);
            pageAbout(activityListVO,page1);
            activityListVO.setResults(page1.getContent());
            return activityListVO;
        }
        //默认和按时间筛选：
        Page<Activity> page = activityRepository.findAll(pageable);
        activityListVO = pageAbout(activityListVO,page);
        activityListVO.setResults(page.getContent());
        return activityListVO;

    }

    /**
     *
     * @param page
     * @return
     */
    public ActivityListVO pageAbout(ActivityListVO activityListVO,Page<Activity> page){
        //总记录数
        activityListVO.setCount((int) page.getTotalElements());
        //当前页
        activityListVO.setCurrent(page.getNumber());
        //末页：
        PageRequest tempPageable = PageRequest.of(page.getTotalPages(),Const.pageSize);
        Page<Activity> tempPage = activityRepository.findAll(tempPageable);

        int nextPage = page.getNumber() <= page.getTotalPages()?page.getNumber()+1:tempPage.getTotalPages();
        int prePage = page.getNumber()>0?page.getNumber()-1:page.getNumber()==0?0:null;
        //下一页
        activityListVO.setNext(Const.pageNext+nextPage);
        //上一页
        activityListVO.setPrevious(Const.pagePre+prePage);
        return activityListVO;
    }

    /**
     * 已报活动列表与历史活动表
     * @param nowPage
     * @param pageSize
     * @param studentId
     * @param status
     * @return
     */
    @Override
    public ActivityListVO getAllReportedActivity(Integer nowPage, int pageSize, String studentId, Short status){


        try{
            List<Activity> activities = activityRepository.findAll();
            List<Activity> activities2 =  new ArrayList<>();
            for (int i = 0;i < activities.size();i++){
                StudentActivities byStudentIdAndActivityId = studentActivitiesRepository.findByStudentIdAndActivityId(studentId, activities.get(i).getUid());
                System.out.println(byStudentIdAndActivityId);
                if(byStudentIdAndActivityId != null){
                    if(byStudentIdAndActivityId.getStatus() == status){
                        activities2.add(activities.get(i));
                    }
                }
            }
            ActivityListVO activityListVO = new ActivityListVO();

            activityListVO.setResults(activities2);

            return activityListVO;
        }
        catch (Exception e){
            System.out.println(e);
            return new ActivityListVO();
        }
    }
}















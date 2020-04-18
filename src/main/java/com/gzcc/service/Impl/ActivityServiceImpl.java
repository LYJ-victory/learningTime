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
        //TODO:选出所有有效的活动：
        Activity activity = new Activity();
        //要匹配的字段：
        activity.setStop(new Short("0"));
        Example<Activity> example1 = Example.of(activity);
        try{
            //1.表示按时间进行排序新活动
            if (condition == 1){
                Sort sort = new Sort(Sort.Direction.ASC, "created");
                PageRequest pageable = new PageRequest(nowPage,pageSize,sort);
                return makeactivityListVO(pageable,example1);
            //2.表示按照类型查询活动
            }else if(condition == 2 && activityType != null){
                List<String> creditTypeList = Arrays.asList("wt_credit","xl_credit","cxcy_credit","fl_credit","sxdd_credit");
                List<String> collect = creditTypeList.stream().filter(type -> type.equals(activityType)).collect(Collectors.toList());
                //TODO:要匹配的字段：
                activity.setCredit_type(collect.get(0));
                ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                        .withMatcher("credit_type",ExampleMatcher.GenericPropertyMatchers.exact())
                        .withMatcher("stop",ExampleMatcher.GenericPropertyMatchers.exact());
                Example<Activity> example = Example.of(activity,exampleMatcher);
                PageRequest pageable = new PageRequest(nowPage,pageSize);
                //得到某一类型的活动列表:
                return makeactivityListVO(pageable,example);
            }
            //默认无条件展示所有新发布的活动：
            PageRequest pageable = new PageRequest(nowPage,pageSize);

            return makeactivityListVO(pageable,example1);
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


        Page<Activity> page = activityRepository.findAll(pageable);

        ActivityListVO activityListVO = new ActivityListVO();
        //总记录数
        activityListVO.setCount((int) page.getTotalElements());
        //当前页
        activityListVO.setCurrent(page.getNumber());
        //末页：
        PageRequest tempPageable = new PageRequest(page.getTotalPages(),Const.pageSize);
        Page<Activity> tempPage = activityRepository.findAll(tempPageable);

        int nextPage = page.getNumber() <= page.getTotalPages()?page.getNumber()+1:tempPage.getTotalPages();
        int prePage = page.getNumber()>0?page.getNumber()-1:page.getNumber()==0?0:null;
        //下一页
        activityListVO.setNext(Const.pageNext+nextPage);
        //上一页
        activityListVO.setPrevious(Const.pagePre+prePage);

        //按学时类型筛选：
        if(example != null){
            activityListVO.setResults(activityRepository.findAll(example,pageable).getContent());
            return activityListVO;
        }
        activityListVO.setResults(activityRepository.findAll(pageable).getContent());

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
//            System.out.println(page);
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















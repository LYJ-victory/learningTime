package com.gzcc.service.Impl;

import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.response.ActivityListVO;
import com.gzcc.repository.ActivityRepository;
import com.gzcc.service.ActivityService;
import com.gzcc.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisService redisService;


    /**
     * 分页展示活动列表：
     * @param nowPage 当前第几页，默认不传的话显示第一页
     * @param condition 按条件查询，默认不按条件查询 1：按照时间查询 2.按照类型查询
     * @param activityType 类型筛选：文体、思想、心理等
     * @return
     */
    @Override
    public ActivityListVO getAllActivity(int nowPage,int pageSize,int condition ,String activityType) {

        //默认展示所有新发布的活动：
        //1.表示按时间进行排序新活动
        //2.表示按照类型查询活动
                //1.表示按照类型查询活动
        try{
            if (condition == 1){//1.表示按时间进行排序新活动

                Sort sort = new Sort(Sort.Direction.ASC, "created");
                PageRequest pageable = new PageRequest(nowPage,pageSize,sort);
                return makeactivityListVO(pageable,null);
            }else if(condition ==2 &&activityType != null){//2.表示按照类型查询活动
                Activity activity = new Activity();
                List<String> creditTypeList = Arrays.asList("wt_credit","xl_credit","cxcy_credit","fl_credit","sxdd_credit");
                List<String> collect = creditTypeList.stream().filter(type -> type.equals(activityType)).collect(Collectors.toList());
                activity.setCredit_type(collect.get(0));
                ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher("credit_type",ExampleMatcher.GenericPropertyMatchers.exact());
                Example<Activity> example = Example.of(activity,exampleMatcher);
                PageRequest pageable = new PageRequest(nowPage,pageSize);
                return makeactivityListVO(pageable,example);//得到某一类型的活动列表
            }
            //默认无条件展示所有新发布的活动：
            PageRequest pageable = new PageRequest(nowPage,pageSize);

            return makeactivityListVO(pageable,null);
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
            System.out.println(activityById.get());
            return activityById.get();
        }catch (Exception e){
            return null;
        }


    }
    @Override
    public int findActivityByUid(String uid) {
        //查找该活动并返回：该活动需要招募的身份是参赛者还是观众

        //缓存中没有：
       try{
           if(StringUtils.isEmpty(redisService.get(uid))){
               final Optional<Activity> activityByUid = activityRepository.findById(uid);
               final int join_type = activityByUid.get().getJoin_type();
//               存放到缓存中：
               redisService.set(uid,String.valueOf(join_type));
               redisService.expire(uid,Const.ACTIVITY_EXPIRE_SECONDS);//设置过期时间是在活动报名结束之前
               return join_type;
           }
       }catch (Exception e){
           return 0;
       }
        //缓存中有：
        String activity_join_type = redisService.get(uid);
        return Integer.valueOf(activity_join_type);

    }
    /**
     * 加入了缓存的活动列表：
     */
//    private ActivityListVO makeRedisActivityListVO(Pageable pageable,Example<Activity> example){
//        //如果example为空，缓存中有就从缓存中拿无条件的page
//        //如果example不为空，缓存中有就从缓存中拿按条件的page
//        if(example == null){
//            final Boolean flag = redisTemplate.hasKey("NoConditionPage");
//            if (flag){
////                Page<Activity> page = redisTemplate.get
//            }
//        }
//
//        Page<Activity> page = activityRepository.findAll(pageable);
//
//        redisTemplate.opsForList().rightPushAll("NoConditionPage",page);
//        ActivityListVO activityListVO = new ActivityListVO();
//        activityListVO.setCount((int) page.getTotalElements());//总记录数
//        activityListVO.setCurrent(page.getNumber());//当前页
//        int nextPage = page.getNumber()<page.getTotalPages()?page.getNumber()+1:page.getNumber() == page.getTotalPages()?page.getTotalPages():null;
//        int prePage = page.getNumber()>0?page.getNumber()-1:page.getNumber()==0?0:null;
//
////        int nextPage = page.getNumber() <= page.getTotalPages()?page.getNumber()+1:null;
////        int prePage = page.getNumber()>0?page.getNumber()-1:page.getNumber()==0?0:null;
//
//
//        activityListVO.setNext(Const.pageNext+nextPage);//下一页
//        activityListVO.setPrevious(Const.pagePre+prePage);//上一页
//        if(example != null){//按学时类型筛选
//            activityListVO.setResults(activityRepository.findAll(example,pageable).getContent());
//            return activityListVO;
//        }
//        activityListVO.setResults(activityRepository.findAll(pageable).getContent());
//
//        return activityListVO;
//    }

    /**
     * 返回活动列表
     * @param pageable
     * @param example
     * @return
     */
    private ActivityListVO makeactivityListVO(Pageable pageable,Example<Activity> example){


        Page<Activity> page = activityRepository.findAll(pageable);

        ActivityListVO activityListVO = new ActivityListVO();
        activityListVO.setCount((int) page.getTotalElements());//总记录数
        activityListVO.setCurrent(page.getNumber());//当前页
//        int nextPage = page.getNumber()<page.getTotalPages()?page.getNumber()+1:page.getNumber() == page.getTotalPages()?page.getTotalPages():null;
//        int prePage = page.getNumber()>0?page.getNumber()-1:page.getNumber()==0?0:null;
        //末页：
        PageRequest tempPageable = new PageRequest(page.getTotalPages(),Const.pageSize);
        Page<Activity> tempPage = activityRepository.findAll(tempPageable);

        int nextPage = page.getNumber() <= page.getTotalPages()?page.getNumber()+1:tempPage.getTotalPages();
        int prePage = page.getNumber()>0?page.getNumber()-1:page.getNumber()==0?0:null;

        activityListVO.setNext(Const.pageNext+nextPage);//下一页
        activityListVO.setPrevious(Const.pagePre+prePage);//上一页
        if(example != null){//按学时类型筛选
            activityListVO.setResults(activityRepository.findAll(example,pageable).getContent());
            return activityListVO;
        }
        activityListVO.setResults(activityRepository.findAll(pageable).getContent());

        return activityListVO;


    }
}















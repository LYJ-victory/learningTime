package com.gzcc.service.Impl;

import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.Student;
import com.gzcc.pojo.StudentActivities;
import com.gzcc.repository.ActivityRepository;
import com.gzcc.repository.StudentActivitiesRepository;
import com.gzcc.repository.StudentRepository;
import com.gzcc.service.StudentActivitiesService;
import com.gzcc.service.StudentService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by ASUS on 2019/12/3.
 */
@Service
public class StudentActivitiesServiceImpl implements StudentActivitiesService {

    private static Logger log = LoggerFactory.getLogger(StudentActivitiesService.class);

    @Autowired
    private StudentActivitiesRepository studentActivitiesRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    @Transactional
    public String InsertStudentId(String uid,String studentId) {

        final String lockKey=new StringBuffer().append(uid).append(studentId).append("-RedissonLock").toString();
        RLock lock=redissonClient.getLock(lockKey);

        try{
            Boolean cacheRes=lock.tryLock(30,10, TimeUnit.SECONDS);

            if(cacheRes){
                //未报名：
                StudentActivities byStudentIdAndActivityId = studentActivitiesRepository.findByStudentIdAndActivityId(studentId, uid);
                if(byStudentIdAndActivityId == null){
                    Integer nums = activityRepository.findById(uid).get().getNums();
                    if(nums == null || nums != 0 ){
                        //可以报名：
                        final Optional<Student> student = studentRepository.findById(studentId);
                        final Optional<Activity> activity = activityRepository.findById(uid);
                        //活动需要人数限制：
                        if(nums != null){
                            activity.get().setNums(activity.get().getNums()-1);
                            activityRepository.save(activity.get());
                        }
                        StudentActivities studentActivities = new StudentActivities();
                        studentActivities.setAcademy(student.get().getAcademy());
                        studentActivities.setGrade(student.get().getGrade());
                        studentActivities.setClazz(student.get().getClazz());
                        studentActivities.setActivityName(activity.get().getName());
                        studentActivities.setActivityId(uid);
                        studentActivities.setStudentId(studentId);
                        studentActivities.setStudentName(student.get().getName());
                        studentActivities.setJoinType(Short.valueOf(String.valueOf(activity.get().getJoin_type())));
                        studentActivities.setStatus(Short.parseShort("1"));
                        LocalDateTime time = LocalDateTime.now();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String createTime = dateTimeFormatter.format(time);
                        studentActivities.setCreateTime(createTime);
                        studentActivities.setCredit(activity.get().getScore());
                        studentActivities.setCreditType(activity.get().getCredit_type());

                        studentActivitiesRepository.save(studentActivities);

                    }
                }else{
                    return Const.FAILED;
                }
            }
        }catch (Exception e){
            log.error("错误信息：{}为止",e.fillInStackTrace());
            return Const.FAILED;
        }finally {
            lock.unlock();
        }

        return Const.SUCCESS;
    }
}

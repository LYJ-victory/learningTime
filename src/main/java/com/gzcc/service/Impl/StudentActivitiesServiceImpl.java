package com.gzcc.service.Impl;

import com.gzcc.pojo.StudentActivities;
import com.gzcc.repository.StudentActivitiesRepository;
import com.gzcc.service.StudentActivitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by ASUS on 2019/12/3.
 */
@Service
public class StudentActivitiesServiceImpl implements StudentActivitiesService {
    @Autowired
    private StudentActivitiesRepository studentActivitiesRepository;

    @Override
//    @Transactional
    public String InsertStudentId(String uid, String activity_join_type,String studentId) {

        //防止重复报名：
            //查看关联表的status是否是已参加：
        final StudentActivities byStudentIdAndActivityId = studentActivitiesRepository.findByStudentIdAndActivityId(studentId, uid);
        if(byStudentIdAndActivityId != null){
            return "不能重复报名";
        }

        StudentActivities studentActivities = new StudentActivities();
        studentActivities.setActivityId(uid);
        studentActivities.setStudentId(studentId);
        studentActivities.setJoinType(Short.valueOf(activity_join_type));
        studentActivities.setStatus(Short.parseShort("1"));//已参加

        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createTime = dateTimeFormatter.format(time);
        studentActivities.setCreateTime(createTime);
         try{

             //TODO
             //TODO
             //数据库插不进去
             final StudentActivities save = studentActivitiesRepository.save(studentActivities);
             System.out.println(save);
         }catch (Exception e){
             return "failed";
         }

        return "success";
    }
}

package com.gzcc.service;

/**
 * Created by ASUS on 2019/12/3.
 */
public interface StudentActivitiesService {
    /**
     * 确认报名
     * @param uid
     * @param studentId
     * @return
     */
    String InsertStudentId(String uid,String activity_join_type, String studentId);
}

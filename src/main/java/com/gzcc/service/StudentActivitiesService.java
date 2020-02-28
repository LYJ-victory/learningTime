package com.gzcc.service;

/**
 * Created by ASUS on 2019/12/3.
 */
public interface StudentActivitiesService {
    /**
     * 确认报名(报名成功)
     * @param uid
     * @param studentId
     * @return
     */
    String InsertStudentId(String uid, String studentId);

}

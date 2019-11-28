package com.gzcc.pojo;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 关联表，学生参加了哪些活动
 */
@Table(name = "studentActivities")
@Entity
public class StudentActivities {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Integer id;
//    报名时间：
    private LocalDateTime createTime;
//    参与类型：
    private String joinType;

    private String studentId;

    private String activityId;
}

package com.gzcc.pojo;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
//    1:参赛者、2：观众、3：工作人员：
//    参与类型：
    private Short joinType;

    private String studentId;

    private String activityId;
    //0:已参加 ,1:未参加：
    @NotEmpty
    private Short status;
}

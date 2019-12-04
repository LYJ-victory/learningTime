package com.gzcc.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * 关联表，学生参加了哪些活动
 */
@Table(name = "studentActivities")
@Entity
@Data
public class StudentActivities {
//    @GeneratedValue这样会与数据库的自增相冲突
    @Id
    private Integer id;
//    报名时间：
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
//    1:参赛者、2：观众、0:无效：
//    参与类型：
    private Short joinType;

    private String studentId;

    private String activityId;
    //0:未参加 ,1:已参加：
    @NotEmpty
    private Short status;
}

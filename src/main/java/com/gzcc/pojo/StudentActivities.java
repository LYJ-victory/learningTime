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
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String academy;

    private String grade;

    private String clazz;

    private String activityName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;
    /**
     * 1:参赛者、2：观众、0:无效：
     */
    private Short joinType;
    private String studentId;
    private String activityId;

    private String studentName;
    /**
     * 0:未参加 ,1:已参加：
     */
    private Short status;

    private Double credit;

    private String creditType;
}

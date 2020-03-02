package com.gzcc.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by ASUS on 2020/2/29.
 * 活动评论
 */
@Table(name = "comment")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue
    private Integer id;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String studentId;

    private String studentName;

    private String studentClass;

    private String studentGrade;

    private String studentAcademy;

    private Integer score;

    private String content;

    private String activityId;

}

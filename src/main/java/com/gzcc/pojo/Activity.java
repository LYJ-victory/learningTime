package com.gzcc.pojo;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.time.LocalDateTime;

/**
 * Created by ASUS on 2019/10/2.
 */
@Table(name = "activity")
@Entity
@Data
public class Activity {
    @Id
    @GeneratedValue
    private String uid;
//    活动名：
    private String name;
//    主办方：
    private String sponsor;
//    活动时间：
    private String time;
//    截止报名时间：
    private LocalDateTime deadline;

    //1:参赛者、2：观众、3：工作人员：
    private Short whoAmI;

////    工作人员学时：
//    private Double scoreStaff;
////    参与者学时：
//    private Double scorePlayer;
////    观众学时：
//    private Double scoreViewer;


//    活动是否结束：
    private Short stop;
//    报名人数：
    private Integer nums;
//    活动地点：
    @Column(columnDefinition = "varchar(200)")
    private String place;
//    活动学时类型：
    private String credit_type;
//    主办方图标：
    private String logo;
//    活动描述：
    private String description;
//    活动创建时间：
    private Date created;
}

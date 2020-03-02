package com.gzcc.pojo.response;

import com.gzcc.pojo.Activity;
import com.gzcc.pojo.Comment;
import lombok.Data;

import java.util.List;

/**
 * Created by ASUS on 2020/3/1.
 */
@Data
public class ActivityReviewVO extends Activity{

    //    活动名：
    private String name;
    //    主办方：
    private String sponsor;
    //    活动时间：
    private String time;

    private String place;
    //活动总分：
    private Integer markScore;

    private List<Comment> list;

}

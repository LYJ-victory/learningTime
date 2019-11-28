package com.gzcc.pojo.response;

import lombok.Data;

import java.util.List;

/**
 * Created by ASUS on 2019/11/21.
 */
@Data
public class ActivityListVO<T> {
    //总记录数：
    private int count;
    //下一页地址：
    private String next;
    //上一页地址：
    private String previous;
    //当前页数：
    private Integer current;
    //活动列表：
    private List<T> results;
}

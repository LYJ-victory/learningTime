package com.gzcc.service;

import com.gzcc.pojo.Activity;
import com.gzcc.pojo.response.ActivityListVO;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by ASUS on 2019/11/20.
 */
public interface ActivityService {

    /**
     * 分页展示活动列表
     * @param nowPage 当前第几页，索引从0开始
     * @param pageSize  每页显示几条数据
     * @param condition
     * @param actovityType
     * @return
     */
    ActivityListVO getAllActivity(int nowPage, int pageSize, int condition , String actovityType);

    /**
     * 活动详情：
     * @param uid 活动主键id
     * @return
     */
    Activity getActivityDetails(String uid);

    int findActivityByUid(String uid);

}

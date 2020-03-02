package com.gzcc.service;

import com.gzcc.pojo.response.ActivityReviewVO;

/**
 * Created by ASUS on 2020/3/1.
 */
public interface CommentService {

    ActivityReviewVO getReviewList(String uid);

    String saveComment(String uid,String studentId, String starScore, String commentContent);
}

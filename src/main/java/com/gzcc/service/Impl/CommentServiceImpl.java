package com.gzcc.service.Impl;

import com.gzcc.common.Const;
import com.gzcc.pojo.Activity;
import com.gzcc.pojo.Comment;
import com.gzcc.pojo.Student;
import com.gzcc.pojo.response.ActivityReviewVO;
import com.gzcc.repository.ActivityRepository;
import com.gzcc.repository.CommentRepository;
import com.gzcc.service.CommentService;
import com.gzcc.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by ASUS on 2020/3/1.
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private StudentService studentService;

    /**
     * 活动评论列表
     * @param uid
     * @return
     */
    @Override
    public ActivityReviewVO getReviewList(String uid) {

        ActivityReviewVO activityReviewVO = new ActivityReviewVO();

        //该活动的信息：
        Optional<Activity> activity = activityRepository.findById(uid);
        BeanUtils.copyProperties(activity.get(),activityReviewVO);
        //活动评论列表：
        //TODO:这里直接扫表，后面要改进下：
        List<Comment> list = commentRepository.findByActivityId(uid);
        activityReviewVO.setList(list);
        //活动总分 =（5*5星评论数+4*4星评论数+3*3星评论数+2*2星评论数+1*1星评论数）/当前总评论数，再四舍五入
        float[] sumScore = {0.0f};
        //<key,value> = <某星评论分，某星评论数*某星评论分>
        list.stream().collect(Collectors.groupingBy(Comment::getScore)).forEach( (k,v) -> {
            int sumGroupScore = k*v.size();
            sumScore[0] +=sumGroupScore;
        });
        //四舍五入：
        sumScore[0] = Math.round(sumScore[0]/list.size());
        activityReviewVO.setMarkScore((int) sumScore[0]);


        return activityReviewVO;
    }

    /**
     * 提交活动评论
     * @param studentId
     * @param starScore
     * @param commentContent
     * @return
     */
    @Override
    public String saveComment(String uid,String studentId, String starScore, String commentContent) {

        Student student = studentService.getMyInformationById(studentId);
        //TODO:评论创建时间和修改时间？
        Comment comment = new Comment(
                null,new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),studentId,
                student.getName(),student.getClazz(),student.getGrade(),student.getAcademy(),
                Integer.valueOf(starScore),commentContent,uid);

        commentRepository.save(comment);

        return Const.SUCCESS;
    }
}

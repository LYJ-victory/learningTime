package com.gzcc.repository;

import com.gzcc.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ASUS on 2020/3/1.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    List<Comment> findByActivityId(String uid);
}

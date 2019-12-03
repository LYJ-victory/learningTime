package com.gzcc.repository;

import com.gzcc.pojo.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by ASUS on 2019/10/2.
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity,String> {

}

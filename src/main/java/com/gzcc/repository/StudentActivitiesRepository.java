package com.gzcc.repository;

import com.gzcc.pojo.StudentActivities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ASUS on 2019/10/2.
 */
@Repository
public interface StudentActivitiesRepository extends JpaRepository<StudentActivities,String>{
}

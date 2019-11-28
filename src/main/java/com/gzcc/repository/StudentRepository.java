package com.gzcc.repository;

import com.gzcc.pojo.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Created by ASUS on 2019/10/2.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student,String> {

    Student findByEmail(String email);

}

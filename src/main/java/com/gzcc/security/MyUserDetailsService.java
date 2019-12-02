package com.gzcc.security;

import com.gzcc.pojo.Student;
import com.gzcc.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by ASUS on 2019/11/17.
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private StudentRepository studentRepository;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<Student> id = studentRepository.findById(s);

        if (id.get().getName() != null){

            return new User(id.get().getUid(),id.get().getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("user"));
        }
        return new User(null,null,null);
    }
}

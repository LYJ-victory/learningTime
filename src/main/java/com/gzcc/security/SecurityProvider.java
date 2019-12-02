package com.gzcc.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by ASUS on 2019/11/28.
 */
public class SecurityProvider implements AuthenticationProvider {
    @Autowired
    private MyUserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
//
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getName());
        if (userDetails == null) {
            throw new UsernameNotFoundException("找不到该用户");
        }
        if(!userDetails.getPassword().equals(token.getCredentials().toString()))
        {
            throw new BadCredentialsException("密码错误");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // TODO Auto-generated method stub
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }

}

package com.mengxuegu.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 查询数据库中的用户信息
 *
 * @author MinQiang
 */
@Slf4j
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("请求认证的用户名：" + username);

        // 1、通过请求的用户名去数据库中查询用户信息
        if (!"meng".equalsIgnoreCase(username)){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        // 假设当前这个用户在数据库当中存储的密码是1234
        String password = passwordEncoder.encode("1234");
        // 2、查询该用户有哪些权限

        // 3、封装用户信息和权限信息
        return new User(username, password,
                AuthorityUtils.commaSeparatedStringToAuthorityList("sys:user,sys:role,ROLE_ADMIN"));
    }
}

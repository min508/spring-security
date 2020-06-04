package com.mengxuegu.security.config;

import com.mengxuegu.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author MinQiang
 */
@Slf4j
@Configuration
@EnableWebSecurity //开启 Spring Security 过滤链 filter
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 配置文件参数
     */
    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private UserDetailsService customUserDetailsService;

    @Resource
    private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Resource
    private AuthenticationFailureHandler customAuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 明文 + 随机盐值 -> 加密存储
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证管理器
     * 1、认证信息（用户名、密码）
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 数据库存储的密码必须是加密后的
        //String password = passwordEncoder().encode("1234");
        //log.info("加密之后存储的密码：" + password);
        //auth.inMemoryAuthentication().withUser("mengxuegu").password(password).authorities("ADMIN");
        auth.userDetailsService(customUserDetailsService);

    }

    /**
     * 当你认证成功之后，spring security 它会重定向到你上一次请求上
     * 资源权限配置：
     * 1、被拦截的资源
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic() // 采用 httpBasic认证方式
        http.formLogin() // 表单登录方式
                .loginPage(securityProperties.getAuthentication().getLoginPage())
                .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl()) // 登录表单提交处理url，默认是 /login
                .usernameParameter(securityProperties.getAuthentication().getUsernameParameter()) // 默认的是 username
                .passwordParameter(securityProperties.getAuthentication().getPasswordParameter())  // 默认的是 password
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .and()
                .authorizeRequests() // 认证请求
                .antMatchers(securityProperties.getAuthentication().getLoginPage()).permitAll()  // 放行 login/page 不需要认证和访问
                .anyRequest().authenticated() // 所有访问该应用的 http 请求都要通过身份认证才可以访问
        ;
    }

    /**
     * 一般是针对静态资源进行放行
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/dist/**","/modules/**","/plugins/**");
    }
}
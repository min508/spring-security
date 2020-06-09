package com.mengxuegu.security.config;

import com.mengxuegu.security.authentication.code.ImageCodeValidateFilter;
import com.mengxuegu.security.authentication.mobile.MobileAuthenticationConfig;
import com.mengxuegu.security.authentication.mobile.MobileValidateFilter;
import com.mengxuegu.security.authentication.session.CustomLogoutHandler;
import com.mengxuegu.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.annotation.Resource;
import javax.sql.DataSource;

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

    @Resource
    private ImageCodeValidateFilter imageCodeValidateFilter;

    /**
     * 校验手机验证码
     */
    @Resource
    private MobileValidateFilter mobileValidateFilter;

    /**
     * 校验手机号是否存在，就是手机号认证
     */
    @Resource
    private MobileAuthenticationConfig mobileAuthenticationConfig;

    @Resource
    private DataSource dataSource;

    /**
     * session 失效后的处理类
     */
    @Resource
    private InvalidSessionStrategy invalidSessionStrategy;

    /**
     * 当同个用户 session 数量超过指定值之后，会调用这个实现类
     */
    @Resource
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    /**
     * 退出之后，将对于的 session 从缓存中清除 SessionRegistryImpl -> ConcurrentMap<Object, Set<String>> principals
     */
    @Resource
    private CustomLogoutHandler customLogoutHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        // 明文 + 随机盐值 -> 加密存储
        return new BCryptPasswordEncoder();
    }

    /**
     * remember_me 记住我功能
     * @return
     */
    @Bean
    public JdbcTokenRepositoryImpl jdbcTokenRepository(){
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 是否启动项目时自动创建表，true 自动创建
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
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
        http.addFilterBefore(mobileValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(imageCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin() // 表单登录方式
                .loginPage(securityProperties.getAuthentication().getLoginPage())
                .loginProcessingUrl(securityProperties.getAuthentication().getLoginProcessingUrl()) // 登录表单提交处理url，默认是 /login
                .usernameParameter(securityProperties.getAuthentication().getUsernameParameter()) // 默认的是 username
                .passwordParameter(securityProperties.getAuthentication().getPasswordParameter())  // 默认的是 password
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailureHandler)
                .and()
                .authorizeRequests() // 认证请求
                .antMatchers(securityProperties.getAuthentication().getLoginPage(),
                        /*"/code/image","/mobile/page","/code/mobile"*/
                        securityProperties.getAuthentication().getImageCodeUrl(),
                        securityProperties.getAuthentication().getMobilePage(),
                        securityProperties.getAuthentication().getMobileCodeUrl()
                ).permitAll()  // 放行 login/page 不需要认证和访问
                .anyRequest().authenticated() // 所有访问该应用的 http 请求都要通过身份认证才可以访问
                .and()
                .rememberMe() // 记住我功能配置
                .tokenRepository(jdbcTokenRepository()) // 保存登录信息
                .tokenValiditySeconds(securityProperties.getAuthentication().getTokenValiditySeconds()) //记住我有效时长
                .and()
                .sessionManagement() // session进行管理
                .invalidSessionStrategy(invalidSessionStrategy) // 当 session 失效后的处理类
                .maximumSessions(1) // 每个用户在系统中最多可以有多少个 session
                .expiredSessionStrategy(sessionInformationExpiredStrategy) // 超过最大数执行这个实现类
                //.maxSessionsPreventsLogin(true) // 当一个用户达到了最大 session 数，则不允许后面再登录
                .sessionRegistry(sessionRegistry())
                .and().and().logout().addLogoutHandler(customLogoutHandler) //退出清除缓存
                .logoutUrl("/user/logout") // 退出请求路径
                .logoutSuccessUrl("/login/page") // 退出成功后跳转地址
                .deleteCookies("JSESSIONID") // 退出后删除什么cookie值
        ;
        http.csrf().disable();// 关闭跨站请求伪造
        // 将手机认证添加到过滤链上
        http.apply(mobileAuthenticationConfig);
    }

    /**
     * 为了解决退出重新登录的问题
     * @return
     */
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    /**
     * 一般是针对静态资源进行放行
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(securityProperties.getAuthentication().getStaticPaths());
    }
}
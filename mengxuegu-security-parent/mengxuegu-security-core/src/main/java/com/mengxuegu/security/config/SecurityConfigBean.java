package com.mengxuegu.security.config;

import com.mengxuegu.security.authentication.mobile.SmsCodeSender;
import com.mengxuegu.security.authentication.mobile.SmsSend;
import com.mengxuegu.security.authentication.session.CustomInvalidSessionStrategy;
import com.mengxuegu.security.authentication.session.CustomSessionInformationExpiredStrategy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.annotation.Resource;

/**
 * 主要为容器中添加 Bean 实例
 *
 * @author MinQiang
 */
@Configuration
public class SecurityConfigBean {

    @Resource
    private SessionRegistry sessionRegistry;

    /**
     * session 超过最大数执行这个实现类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
        return new CustomSessionInformationExpiredStrategy();
    }

    /**
     * 当 session 失效后的处理类
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy(){
        return new CustomInvalidSessionStrategy(sessionRegistry);
    }

    /**
     * @ConditionalOnMissingBean(SmsSend.class)
     * 默认情况下，采用的是 SmsCodeSender 实例，
     * 但是如果容器当中有其他的 SmsSend 类型的实例，
     * 那当前的这个 SmsCodeSender 就失效了
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SmsSend.class)
    public SmsSend smsSend(){
        return new SmsCodeSender();
    }
}

package com.mengxuegu.security.config;

import com.mengxuegu.security.mobile.SmsCodeSender;
import com.mengxuegu.security.mobile.SmsSend;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 主要为容器中添加 Bean 实例
 *
 * @author MinQiang
 */
@Configuration
public class SecurityConfigBean {

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

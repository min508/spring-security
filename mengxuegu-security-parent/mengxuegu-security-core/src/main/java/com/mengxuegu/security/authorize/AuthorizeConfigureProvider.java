package com.mengxuegu.security.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * 授权配置统一接口，所有模块的授权配置类都要实现这个接口
 *
 * @author MinQiang
 */
public interface AuthorizeConfigureProvider {

    void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}

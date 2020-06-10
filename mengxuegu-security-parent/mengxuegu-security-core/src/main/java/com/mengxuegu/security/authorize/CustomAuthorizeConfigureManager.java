package com.mengxuegu.security.authorize;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 授权配置统一接口，所有模块的授权配置类都要实现这个接口
 *
 * @author MinQiang
 */
@Component
public class CustomAuthorizeConfigureManager implements AuthorizeConfigureManager {

    @Resource
    List<AuthorizeConfigureProvider> authorizeConfigureProviders;

    /**
     * 将一个个 AuthorizeConfigureProvider 的实现类，传入配置的参数 ExpressionInterceptUrlRegistry
     * @param config
     */
    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        for (AuthorizeConfigureProvider provider : authorizeConfigureProviders){
            provider.configure(config);
        }
    }
}

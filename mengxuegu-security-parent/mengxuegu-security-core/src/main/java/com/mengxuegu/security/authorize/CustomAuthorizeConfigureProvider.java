package com.mengxuegu.security.authorize;

import com.mengxuegu.security.properties.SecurityProperties;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * 身份认证相关的授权配置
 *
 * @author MinQiang
 */
@Component
@Order(Integer.MAX_VALUE) // 值越小加载越优先，值越大加载越靠后
public class CustomAuthorizeConfigureProvider implements AuthorizeConfigureProvider {

    @Resource
    private SecurityProperties securityProperties;

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(securityProperties.getAuthentication().getLoginPage(),
                /*"/code/image","/mobile/page","/code/mobile"*/
                securityProperties.getAuthentication().getImageCodeUrl(),
                securityProperties.getAuthentication().getMobilePage(),
                securityProperties.getAuthentication().getMobileCodeUrl()
        ).permitAll();  // 放行 login/page 不需要认证和访问

        // 其他请求都要通过身份认证
        config.anyRequest().authenticated();
    }
}

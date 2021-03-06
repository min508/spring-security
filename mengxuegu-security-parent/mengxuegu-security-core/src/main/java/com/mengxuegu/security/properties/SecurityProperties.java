package com.mengxuegu.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @author MinQiang
 */
@Component
@ConfigurationProperties(prefix = "mengxuegu.security")
public class SecurityProperties {

    /**
     * 会将 mengxuegu.security.authentication 下面的值绑定到 AuthenticationProperties 上
     */
    private AuthenticationProperties authentication;

    public AuthenticationProperties getAuthentication() {
        return authentication;
    }

    public void setAuthentication(AuthenticationProperties authentication) {
        this.authentication = authentication;
    }
}

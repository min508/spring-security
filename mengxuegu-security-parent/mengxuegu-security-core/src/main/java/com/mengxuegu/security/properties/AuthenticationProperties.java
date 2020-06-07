package com.mengxuegu.security.properties;

import lombok.Data;

/**
 * TODO
 *
 * @author MinQiang
 */
@Data
public class AuthenticationProperties {

    private String loginPage = "/login/page";
    private String loginProcessingUrl = "/login/form";
    private String usernameParameter = "name";
    private String passwordParameter = "pwd";
    private String[] staticPaths = {"/dist/**", "/modules/**", "/plugins/**"};
    /**
     * 认证响应的类型：JSON / REDIRECT
     */
    private LoginResponseType loginType = LoginResponseType.REDIRECT;

    private String imageCodeUrl = "/code/image";
    private String mobileCodeUrl = "/code/mobile";
    private String mobilePage = "/mobile/page";
    private Integer tokenValiditySeconds = 60*60*24*7;

}

package com.mengxuegu.security.authentication.session;

import com.mengxuegu.security.authentication.CustomAuthenticationFailureHandler;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 当同一用户的 session 达到指定数量时，会执行该类
 *
 * @author MinQiang
 */
public class CustomSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Resource
    private CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        // 1、获取用户名
        UserDetails userDetails = (UserDetails) event.getSessionInformation().getPrincipal();

        AuthenticationException exception = new AuthenticationServiceException(
                String.format("【%s】用户在另外一台电脑上登录，您已被下线", userDetails.getUsername()));

        try {
            // 当用户在另外一台电脑登录后，交给失败处理器回到认证页面
            event.getRequest().setAttribute("toAuthentication", true);
            customAuthenticationFailureHandler.onAuthenticationFailure(event.getRequest(), event.getResponse(), exception);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}

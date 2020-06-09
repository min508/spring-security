package com.mengxuegu.security.authentication.session;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author MinQiang
 */
@Component
public class CustomLogoutHandler implements LogoutHandler {

    @Resource
    private SessionRegistry sessionRegistry;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        /**
         * 退出之后，将对于的 session 从缓存中清除 SessionRegistryImpl -> ConcurrentMap<Object, Set<String>> principals
         */
        sessionRegistry.removeSessionInformation(request.getSession().getId());
    }
}

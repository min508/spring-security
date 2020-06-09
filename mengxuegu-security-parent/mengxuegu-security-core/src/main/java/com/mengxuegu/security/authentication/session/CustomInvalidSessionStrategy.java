package com.mengxuegu.security.authentication.session;

import com.mengxuegu.base.result.MengxueguResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 当session是失效后的处理逻辑
 *
 * @author MinQiang
 */
@Slf4j
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {

    private SessionRegistry sessionRegistry;

    public CustomInvalidSessionStrategy(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * 超时之后调用的方法
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info(request.getSession().getId());  // 超时之后 session 清除，没有就创建一个新的，有就返回原值
        log.info(request.getRequestedSessionId()); // 从浏览器 Cookie 中获取的那个 session id
        sessionRegistry.removeSessionInformation(request.getSession().getId());
        // 要将浏览器中的 JSESSIONID 删除
        cancelCookie(request, response);

        MengxueguResult result =
                new MengxueguResult().build(HttpStatus.UNAUTHORIZED.value(), "登录已超时，请重新登录");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(result.toJsonString());

    }

    protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setMaxAge(0);
        cookie.setPath(getCookiePath(request));
        response.addCookie(cookie);
    }

    private String getCookiePath(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        return contextPath.length() > 0 ? contextPath : "/";
    }
}

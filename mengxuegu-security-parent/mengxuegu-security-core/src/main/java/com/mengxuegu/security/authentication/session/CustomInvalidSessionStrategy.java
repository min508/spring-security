package com.mengxuegu.security.authentication.session;

import com.mengxuegu.base.result.MengxueguResult;
import org.springframework.http.HttpStatus;
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
public class CustomInvalidSessionStrategy implements InvalidSessionStrategy {
    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

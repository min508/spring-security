package com.mengxuegu.security.authentication;

import com.mengxuegu.base.result.MengxueguResult;
import com.mengxuegu.security.properties.LoginResponseType;
import com.mengxuegu.security.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理失败认证的
 *
 * @author MinQiang
 */
@Component
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Resource
    private SecurityProperties securityProperties;

    /**
     * @param request
     * @param response
     * @param e        认证失败是抛出的异常
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        if (LoginResponseType.JSON.equals(securityProperties.getAuthentication().getLoginType())) {
            // 认证失败响应的 JSON 的字符串
            MengxueguResult result = MengxueguResult.build(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(result.toJsonString());
        }else {
            // 重定向回认证页面，注意加上"?error"
            //super.setDefaultFailureUrl(securityProperties.getAuthentication().getLoginPage()+"?error");
            // 获取上一次请求的路径
            String referer = request.getHeader("Referer");
            logger.info("referer：" + referer);
            Object toAuthentication = request.getAttribute("toAuthentication");
            String lastUrl = toAuthentication != null ? securityProperties.getAuthentication().getLoginPage()
                    : StringUtils.substringBefore(referer, "?");
            logger.info("上一次请求的路径：" +  lastUrl);
            super.setDefaultFailureUrl(lastUrl + "?error");
            super.onAuthenticationFailure(request, response, e);
        }
    }
}

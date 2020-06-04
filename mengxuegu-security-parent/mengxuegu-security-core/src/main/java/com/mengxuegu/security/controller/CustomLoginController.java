package com.mengxuegu.security.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * TODO
 *
 * @author MinQiang
 */
@Slf4j
@Controller
public class CustomLoginController {

    public static final String SESSION_KEY = "SESSION_KEY_IMAGE_CODE";

    @Resource
    private DefaultKaptcha defaultKaptcha;

    @RequestMapping("/login/page")
    public String toLogin(){
        return "login";  // classpath:/templates/login.html
    }

    /**
     * 获取图像验证码
     */
    @RequestMapping("/code/image")
    public void imageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1、获取验证码字符串
        String code = defaultKaptcha.createText();
        log.info("生成的图形验证码是：" + code);
        // 2、字符串把它放入 session 中
        request.getSession().setAttribute(SESSION_KEY, code);
        // 3、获取验证码图片
        BufferedImage image = defaultKaptcha.createImage(code);
        // 4、将验证码图片把它写出去
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "jpg", out);
    }
}

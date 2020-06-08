package com.mengxuegu.security;

import com.mengxuegu.security.authentication.mobile.SmsSend;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO
 *
 * @author MinQiang
 */
@Slf4j
//@Component
public class MobileSmsCodeSender implements SmsSend {
    @Override
    public boolean sendSms(String mobile, String content) {
        log.info("Web应用向手机号：【" + mobile +"】发送了新的短信验证码接口：" + content);
        return false;
    }
}

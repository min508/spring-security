package com.mengxuegu.security.mobile;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送短信验证码，第三方服务接口
 *
 * @author MinQiang
 */
@Slf4j
public class SmsCodeSender implements SmsSend{
    /**
     *
     * @param mobile 手机号
     * @param content 发送的内容
     * @return
     */
    @Override
    public boolean sendSms(String mobile, String content) {
        String sendContent = String.format("梦学谷，验证码：%s，请勿泄露给别人", content);
        // 调用第三方发送功能的SDK
        log.info("向手机号：" + mobile + "发送的短信为：" + sendContent);
        return true;
    }
}

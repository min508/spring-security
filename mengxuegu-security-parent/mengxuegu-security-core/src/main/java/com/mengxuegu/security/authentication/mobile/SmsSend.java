package com.mengxuegu.security.authentication.mobile;

/**
 * 短信发送统一接口
 *
 * @author MinQiang
 */
public interface SmsSend {

    /**
     * 发送短信
     * @param mobile 手机号
     * @param content 发送的内容
     * @return true 标识发送成功，false 表示发送失败
     */
    boolean sendSms(String mobile, String content);
}

package com.mengxuegu.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 用户管理
 *
 * @author MinQiang
 */
@Controller
@RequestMapping("/user")
public class SysUserController {

    private static final String HTML_PREFIX="system/user/";

    @PreAuthorize("hasAuthority('sys:user')")
    @GetMapping(value = {"/",""})
    public String user(){
            return HTML_PREFIX + "user-list";
    }
}

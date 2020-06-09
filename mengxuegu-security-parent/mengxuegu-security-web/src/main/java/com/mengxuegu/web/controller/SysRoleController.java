package com.mengxuegu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 角色管理
 *
 * @author MinQiang
 */ 
@Controller
@RequestMapping("/role")
public class SysRoleController {

    private static final String HTML_PREFIX="system/role/";

    @GetMapping(value = {"/",""})
    public String role(){
            return HTML_PREFIX + "role-list";
    }
}

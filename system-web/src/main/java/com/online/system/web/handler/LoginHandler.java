package com.online.system.web.handler;

import com.online.system.annotation.OperLog;
import com.online.system.web.entity.Result;
import com.online.system.web.service.LoginService;
import com.online.system.common.utils.JasonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录
 * 使用spring security 由此采用这种方式登录
 */
@RestController
public class LoginHandler {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登陆
     * @param request
     * @return
     */
    @PostMapping("login")
    @OperLog(module="登陆",type = "",desc = "用户登录系统")
    public String login(HttpServletRequest request, HttpServletResponse response){
        Result<String> result = loginService.login(request,response);
        return JasonUtil.obj2String(result);
    }
}

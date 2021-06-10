package com.online.system.web.service.impl;

import com.online.system.web.entity.Result;
import com.online.system.web.security.JwtAuthenticationToken;
import com.online.system.web.service.LoginService;
import com.online.system.common.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆实现类
 */
@Service
public class LoginServiceImpl implements LoginService{

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Result<String> login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        Result<String> result = new Result<>();
        String msg = "登录失败！";
        int code = 1;
        String jwtToken = null;
        // 系统登录认证
        JwtAuthenticationToken token = SecurityUtil.login(request, username, password, authenticationManager);
        if(token!=null){
            jwtToken = token.getToken();
            code = 0;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setData(jwtToken);
        return result;
    }
}

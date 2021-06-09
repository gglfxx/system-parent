package com.online.system.web.security;

import com.online.system.common.utils.JasonUtil;
import com.online.system.common.utils.JwtTokenUtils;
import com.online.system.web.entity.Result;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 自定义处理登出
 * 处理手动跳转到登录页
 * 前后端分离状态返回
 */
public class LogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        response.sendRedirect(request.getContextPath()+"/login.html");
        //String token = JwtTokenUtils.getToken(request);
        /*String token = getCookie()
        Claims claims = JwtTokenUtils.getTokenClaim(token);
        //设置失效时间为当前时间
        claims.setExpiration(new Date());*/
        //给前端返回状态 自行跳转页面
       /* response.setContentType("application/json; charset=utf-8");
        Result<String> result = new Result<>();
        result.setMsg("退出登陆成功");
        result.setCode(0);
        response.getWriter().print(JasonUtil.obj2String(result));
        response.getWriter().flush();
        response.getWriter().close();*/
    }
}

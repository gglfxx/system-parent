package com.online.system.web.security;

import com.online.system.common.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 自定义处理登出
 */
public class LogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        String token = JwtTokenUtils.getToken(request);
        Claims claims = JwtTokenUtils.getTokenClaim(token);
        //设置失效时间为当前时间
        claims.setExpiration(new Date());
    }
}

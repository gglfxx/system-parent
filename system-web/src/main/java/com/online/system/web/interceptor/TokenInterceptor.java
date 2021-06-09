package com.online.system.web.interceptor;

import com.online.system.web.entity.Result;
import com.online.system.common.utils.JasonUtil;
import com.online.system.common.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;

/**
 * jwt拦截
 * use jwt to be token invalidate
 */
@Component
@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws SignatureException {
        /**
         * 从请求头获取token值或通过传参
         */
        String token = JwtTokenUtils.getToken(request);
        Result<String> result = new Result<>();
        try{
            //防止中文乱码
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Type","text/html;charset=utf-8");
            if(StringUtils.isBlank(token)){
                //判断是否是ajax请求
                if (isAjax(request)) {
                    result.setCode(1001);
                    result.setMsg("token不能为空");
                    response.getWriter().print(JasonUtil.obj2String(result));
                }else{
                    response.sendRedirect(request.getContextPath()+"/login.html");
                }
                return false;
            }else{
                Claims claims = null;
                claims = JwtTokenUtils.getTokenClaim(token);
                if(claims == null || JwtTokenUtils.isTokenExpired(token)){
                    if (isAjax(request)) {
                        result.setCode(1001);
                        result.setMsg("token失效，请重新登录。");
                        response.getWriter().print(JasonUtil.obj2String(result));
                    }else{
                        response.sendRedirect(request.getContextPath()+"/login.html");
                    }
                    return false;
                }
            }
        }catch (Exception e){
            throw new SignatureException("token失效，请重新登录。");
        }
        return true;
    }
    /**
     * 判断是否是ajax请求
     * @param request
     * @return
     */
    private boolean isAjax(HttpServletRequest request){
        String header = request.getHeader("x-requested-with");
        if (header != null && header.equalsIgnoreCase("XMLHttpRequest"))
            return true;
        return false;
    }
}

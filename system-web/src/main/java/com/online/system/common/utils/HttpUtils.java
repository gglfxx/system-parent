package com.online.system.common.utils;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.online.system.web.entity.Result;
import com.online.system.web.entity.ResultStatus;
import com.online.system.web.security.JwtAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * HTTP工具类
 */
public class HttpUtils {

	/**
	 * 获取HttpServletRequest对象
	 * @return
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}
	
	/**
	 * 输出信息到浏览器
	 * @param response
	 * @param data
	 * @throws IOException
	 */
	public static void write(HttpServletResponse response, Object data) throws IOException {
		JwtAuthenticationToken token = null;
		AuthenticationException failed = null;
		int code = ResultStatus.SUCCESS;
		String msg = null;
		String jwtToken = null;
		//处理登陆成功后
		if(data instanceof JwtAuthenticationToken){
			token = (JwtAuthenticationToken) data;
			jwtToken = token.getToken();
		}else if(data instanceof AuthenticationException){
			//处理登陆失败后
			failed = (AuthenticationException) data;
			code = ResultStatus.ERROR;
			msg = failed.getMessage();
		}
		response.setContentType("application/json; charset=utf-8");
        Result<String> result = new Result<>();
        result.setData(jwtToken);
        result.setMsg(msg);
        result.setCode(code);
        response.getWriter().print(JasonUtil.obj2String(result));
        response.getWriter().flush();
        response.getWriter().close();
	}
	
}

package com.online.system.web.service;

import com.online.system.web.entity.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆
 */
public interface LoginService {
    Result<String> login(HttpServletRequest request, HttpServletResponse response);
}

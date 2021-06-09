package com.online.system.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * cookies工具类
 */
public class CookieUtil {
	
    /**
     * 设置局部cookie
     * @param response
     * @param key
     * @param value
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiry){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        response.addCookie(cookie);
    }
    
    /**
     * 设置局部cookie增加isHttpOnly
     * @param response
     * @param key
     * @param value
     * @param expiry
     * @param isHttpOnly
     */
    public static void setCookie(HttpServletResponse response, String key, String value, int expiry,boolean isHttpOnly){
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }
    
    /**
     * 可设置全局cookie
     * @param response
     * @param key
     * @param value
     * @param path
     * @param expiry
     */
    public static void setCookie(HttpServletResponse response, String key, String value,String path,int expiry){
    	Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /***
     * 设置cookie
     * @param response
     * @param key
     * @param value
     * @param path 	路径
     * @param expiry 时间
     * @param isHttpOnly 设置是否支持HttpOnly属性
     */
    public static void setCookie(HttpServletResponse response, String key, String value,String path,int expiry,boolean isHttpOnly){
    	Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(expiry);
        cookie.setPath(path);
        cookie.setHttpOnly(isHttpOnly);
        response.addCookie(cookie);
    }
    /**
     * 获取单个key
     * @param key
     * @return
     */
    public static String getCookie(HttpServletRequest request,String key){
    	if (request == null) {
    		return null;
    	}
        Cookie cookies[] = request.getCookies();
        if (cookies != null){
            for(int i = 0; i < cookies.length; i++){
                if(cookies[i].getName().equals(key)){
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    /**
     * 清除单个key
     * @param response
     * @param key
     */
    public static void clearCookie(HttpServletResponse response,String key){
        Cookie cookie = new Cookie(key, null);
        //将`Max-Age`设置为0
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
    
    /**
     * 清除自定义全局或局部内的cookie
     * @param response
     * @param key
     * @param path
     */
    public static void clearCookie(HttpServletResponse response,String key,String path){
        Cookie cookie = new Cookie(key, null);
        //将`Max-Age`设置为0
        cookie.setMaxAge(0);
        cookie.setPath(path);
        response.addCookie(cookie);
    }

    /**
     * 获取除JSESSION的cookies
     * @param request
     * @return
     */
    public static Map<String, String> getCookies(HttpServletRequest request){
        Map<String, String> map = new HashMap<>();
        Cookie cookies[] = request.getCookies();
        if (cookies != null){
            for(int i = 0; i < cookies.length; i++){
                if(!"JSESSION".equals(cookies[i].getName()))
                    map.put(cookies[i].getName(), cookies[i].getValue());
            }
        }
        return map;
    }

    //清空所有的Cookie
    public static void clearCookies(HttpServletRequest request, HttpServletResponse response){
        Map<String, String> map = getCookies(request);
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, String> me = iterator.next();
            Cookie cookie = new Cookie(me.getKey(), "");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
}

package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {

    private final static String COOKIE_DOMAIN=".jsf.com";
    private final static String COOKIE_NAME = "mmall_login_token";

    public static void writeLoginToken(HttpServletResponse response,String token){
        Cookie cookie = new Cookie(COOKIE_NAME,token);
        cookie.setDomain(COOKIE_DOMAIN);
        cookie.setPath("/");//代表设置在根目录
        //单位是秒
        //如果这个maxAge不设置的话，cookie就不会写入硬盘，而是写在内存，只在当前页面有效
        //如果是-1  代表永久
        cookie.setMaxAge(60*60*24*365);
        log.info("write cookieName : {},cookieValue : {}",cookie.getName(),cookie.getValue());
        response.addCookie(cookie);
    }

    public static String readLoginToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null && cookies.length>0){
            for(int i=0;i<cookies.length;i++){
                log.info("read cookieName : {},cookieValue : {}",cookies[i].getName(),cookies[i].getValue());
                if(StringUtils.equals(cookies[i].getName(),COOKIE_NAME)){
                    log.info("return cookieName : {},cookieValue : {}",cookies[i].getName(),cookies[i].getValue());
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookies = request.getCookies();
        if(cookies!=null){
            for(Cookie cookie:cookies){
                if(StringUtils.equals(cookie.getName(),COOKIE_NAME)){
                    cookie.setDomain(COOKIE_DOMAIN);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);//设置为0 代表删除此cookie
                    log.info("del cookieName : {},cookieValue : {}",cookie.getName(),cookie.getValue());
                    response.addCookie(cookie);
                }
            }
        }
    }
}

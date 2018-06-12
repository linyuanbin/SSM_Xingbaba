package com.lyb.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 获取CSESSIONID
 * Created by lin on 2018/4/19.
 */
public class RequestUtils {

    public static String getCSESSIONID(HttpServletRequest request, HttpServletResponse response){
        //1.取出Cookie
        Cookie[] cookies = request.getCookies();
        if(null != cookies && cookies.length > 0){
            for(Cookie cookie:cookies){
                //2.判断Cookie中是否有CSESSIONID
                if("CSESSIONID".equals(cookie.getName())){
                    //3.有 直接使用
                    return cookie.getValue();
                }
            }
        }
        //4.没有 创建一个CSESSIONID 并且保存到Cookie中，并将Cookie写回到浏览器
        String csessionid = UUID.randomUUID().toString().replace("-", "");//UUID生成的唯一主键中含有‘-’,需要去除
        Cookie cookie=new Cookie("CSESSIONID",csessionid); //创建一个新的cookie     //java线程中cookie中存放的session标识默认名是JSESSIONID 为了不被自动创建，自定义名称为了CSESSIONID
        //设置存活时间  默认值三种  -1：关闭浏览器销毁   0: 立即销毁       >0 :设置多长时间就是多长时间
        cookie.setMaxAge(-1);
        //设置路径
        cookie.setPath("/"); //设置成‘/’ 使得不论是 /portal 还是 /login 请求 Request对象都会携带着cookie
        //设置跨域 localhost == search.jd.com   www.jd.com item.jd.com
        //cookie.setDomain(".jd.com");
        //写回浏览器
        response.addCookie(cookie);
        return csessionid;
    }


}

package com.lyb.core.interceptor;

import com.lyb.common.utils.RequestUtils;
import com.lyb.core.service.user.SessionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义池里类  拦截的是Controller之前       Springmvc Handler处理器
 * Created by lin on 2018/4/23.
 */
public class CostomInterceptor implements HandlerInterceptor{

    @Autowired
    private SessionProvider sessionProvider;

    //Controller之前执行
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //必须登录
        String userName = sessionProvider.getAttributeForUserName(RequestUtils.getCSESSIONID(request, response));
        if(null == userName){
            //不放行
            //重定向会登录界面
            response.sendRedirect("http://localhost:8081/login.aspx?returnUrl=http://localhost:8082"); //returnUrl 设置登录后是返回首页还是购物车界面
            return false;
        }
        //放行
        return true;
    }

    //Controller之后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    //页面渲染之后  视图解析器之后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

package com.lyb.core.service.user;

/**
 * Created by lin on 2018/4/18.
 */
public interface SessionProvider {

    //保存用户名到redis
    public void setAttributeForUserName(String name, String value);

    //从redis中取出用户名
    public String getAttributeForUserName(String name);

}

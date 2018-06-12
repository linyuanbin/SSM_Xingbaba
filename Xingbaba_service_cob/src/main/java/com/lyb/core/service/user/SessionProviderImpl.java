package com.lyb.core.service.user;

import com.lyb.common.web.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

/**
 * Created by lin on 2018/4/18.
 */
public class SessionProviderImpl implements SessionProvider{
    @Autowired
    private Jedis jedis;
    private Integer exp=30;//默认时间30分钟
    public void setExp(Integer exp) {
        this.exp = exp;
    }
    @Override
    public void setAttributeForUserName(String name, String value) { // name：CSESSIONID    value:是用户名
        //保存用户名到redis
        //key : CSESSIONID:USER_NAME == name
        jedis.set(name+":"+ Constants.USER_NAME,value);
        jedis.expire(name+":"+ Constants.USER_NAME,60*exp); //设置key在redis中的有效时间 单位是秒
    }

    @Override
    public String getAttributeForUserName(String name) {
        String value = jedis.get(name + ":" + Constants.USER_NAME);
        if(null!= value){
            jedis.expire(name+":"+ Constants.USER_NAME,60*exp); //设置key在redis中的有效时间 单位是秒
        }
        return value;
    }
}

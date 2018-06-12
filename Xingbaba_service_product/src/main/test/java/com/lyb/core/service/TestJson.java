package com.lyb.core.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyb.core.bean.user.Buyer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by lin on 2018/3/30.
 */

public class TestJson {

    @Test
    public void testJson()throws Exception{
        //springmvc 中 @RequestBody  @ResponseBody  Json和对象转换
        Buyer buyer=new Buyer();
        buyer.setUsername("是否能");
        ObjectMapper om=new ObjectMapper();
        //设置转换的Json字符串中不包含Null 的部分属性
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        StringWriter stringWriter=new StringWriter();
        //oj.writeValueAsString(buyer); //这种方式是一次性转换全部，不推荐使用，容易造成内存溢出（OOM）


            om.writeValue(stringWriter,buyer);
            System.out.println(stringWriter.toString());//{"username":"是否能"}

        //转回对象
        Buyer buyer1 = om.readValue(stringWriter.toString(), Buyer.class);
        System.out.println(buyer1.toString());

    }

}

package com.lyb.core.service;

import com.lyb.core.bean.BuyerItem;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.bean.user.Buyer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin on 2018/3/30.
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})*/
public class redisTest {
/*    @Autowired
    private Jedis jedis;

    @Test
    public void testSpringJedis() {
        jedis.set("you", "5566");
    }

    @Test
    public void testRedis(){
        Jedis jedis=new Jedis("192.168.200.128",6379);
        Long pno = jedis.decr("pno");
        System.out.println(pno);
    }*/

    @Test
    public void testEquals(){

        List<BuyerItem> list=new ArrayList<>();
        BuyerItem buyerItem1=new BuyerItem();
        Sku sku=new Sku();
        sku.setId(12L);
        buyerItem1.setSku(sku);
        list.add(buyerItem1);
        BuyerItem buyerItem2=new BuyerItem();
        buyerItem2.setSku(sku);
        System.out.println(list.contains(buyerItem2));
        System.out.println(buyerItem1.equals(buyerItem2));
        System.out.println(buyerItem1==buyerItem2);
        BuyerItem buyerItem3=new BuyerItem();
        buyerItem3.setSku(sku);
        buyerItem1=buyerItem3;
        buyerItem2=buyerItem3;
        System.out.println(buyerItem1==buyerItem2);
        System.out.println(buyerItem1.hashCode()+"  "+buyerItem2.hashCode());
    }

}

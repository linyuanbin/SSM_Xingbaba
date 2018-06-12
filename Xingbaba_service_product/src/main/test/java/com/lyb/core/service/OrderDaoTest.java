package com.lyb.core.service;

import com.lyb.core.bean.BuyerItem;
import com.lyb.core.bean.order.Order;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.dao.order.OrderDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lin on 2018/3/30.
 */
public class OrderDaoTest {

    @Autowired
    private OrderDao orderDaoo;

    @Test
    public void testOrderDao(){
        Order order=new Order();
        order.setId(999L);
        order.setCreateDate(new Date());
        orderDaoo.insertSelective(order); //NullPointerException
    }

}

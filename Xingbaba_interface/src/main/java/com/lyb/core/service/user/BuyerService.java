package com.lyb.core.service.user;

import com.lyb.core.bean.order.Order;
import com.lyb.core.bean.user.Buyer;

/**
 * Created by lin on 2018/4/18.
 */
public interface BuyerService {
    ////通过用户名查用户对象
    public Buyer selectBuyerByUserName(String userName);

    //订单提交
    public void  insertOrder(Order order, String username);
}

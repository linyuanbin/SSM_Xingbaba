package com.lyb.core.service.product;

import com.lyb.core.bean.BuyerCar;
import com.lyb.core.bean.product.Sku;

import java.util.List;

/**
 * Created by lin on 2018/3/30.
 */
public interface SkuService {
    //查询库存列表
    public List<Sku> selectSkuListByProductId(Long productId);
    //修改库存
    public void updateSkuById(Sku sku);
    //通过skuid查询sku对象、product对象、Color对象，且封装在sku对象中  装满购物车
    public Sku selectSkuById(Long id);
    //保存购物车中商品到redis中购物车
    public void insertBuyerCartToRedis(BuyerCar buyerCar, String userName);
    //从Redis购物车中奖商品取出
    public BuyerCar selectBuyerCartFromRedis(String username);

}

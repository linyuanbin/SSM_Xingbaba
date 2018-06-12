package com.lyb.core.service.user;

import com.lyb.core.bean.BuyerCar;
import com.lyb.core.bean.BuyerItem;
import com.lyb.core.bean.order.Detail;
import com.lyb.core.bean.order.Order;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.bean.user.Buyer;
import com.lyb.core.bean.user.BuyerQuery;
import com.lyb.core.dao.order.DetailDao;
import com.lyb.core.dao.order.OrderDao;
import com.lyb.core.dao.product.ColorDao;
import com.lyb.core.dao.product.ProductDao;
import com.lyb.core.dao.product.SkuDao;
import com.lyb.core.dao.user.BuyerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户管理
 * Created by lin on 2018/4/18.
 */
@Service("buyerService")
public class BuyerServieImpl implements BuyerService {

    @Autowired
    private BuyerDao buyerDao;
    //通过用户名查用户对象
    public Buyer selectBuyerByUserName(String userName){
        BuyerQuery buyerQuery=new BuyerQuery();
        buyerQuery.createCriteria().andUsernameEqualTo(userName);
        List<Buyer> buyers = buyerDao.selectByExample(buyerQuery);
        if(null!=buyers && buyers.size()>0){
            return buyers.get(0);
        }
        return null;
    }

    @Autowired
    private Jedis jedis;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private DetailDao detailDao;
    //订单保存
    public void  insertOrder(Order order,String username){
        //保存订单
        //订单ID（订单编号） 全国唯一 有redis生成
        Long id = jedis.incr("oid");
        order.setId(id);
        //加载购物车 获取需要信息
        BuyerCar buyerCar = selectBuyerCartFromRedis(username);

        List<BuyerItem> items = buyerCar.getItems();
        for(BuyerItem buyerItem:items){
            //将购物车装满
            buyerItem.setSku(selectSkuById(buyerItem.getSku().getId()));
        }
        //运费  由购物车提供
        order.setDeliverFee(buyerCar.getFee());
        //总价
        order.setTotalPrice(buyerCar.getTotalPrice());
        //订单金额
        order.setOrderPrice(buyerCar.getProductPrice());
        //支付方式 0:到付 1:在线 2:邮局 3:公司转帐
        //支付状态 :0到付1待付款,2已付款,3待退款,4退款成功,5退款失败   支付状态受支付方式限制
        if(order.getPaymentWay() == 1){
            order.setIsPaiy(0);
        }else {
            order.setIsPaiy(1);
        }
        //订单状态 0:提交订单 1:仓库配货 2:商品出库 3:等待收货 4:完成 5待退货 6已退货
        order.setOrderState(0);
        //时间  后台程序自己写
        order.setCreateDate(new Date());
        //用户ID  系统在用户注册是会以  用户名:用户Id 形式保存到redis中
        String userid = jedis.get(username);
        order.setBuyerId(Long.parseLong(userid));
        //保存订单   ---------------订单结束
        orderDao.insertSelective(order);

        //保存订单详情
        for(BuyerItem buyerItem:items){
            Detail detail=new Detail();
            //ID
            //订单ID
            detail.setOrderId(id);
            //商品编号
            detail.setProductId(buyerItem.getSku().getProductId());
            //商品名称
            detail.setProductName(buyerItem.getSku().getProduct().getName());
            //颜色
            detail.setColor(buyerItem.getSku().getColor().getName());
            //尺码
            detail.setSize(buyerItem.getSku().getSize());
            //价格
            detail.setPrice(buyerItem.getSku().getPrice());
            //数量
            detail.setAmount(buyerItem.getAmount());
            //都由购物车提供
            detailDao.insertSelective(detail);
        }

        //订单提交完 清空redis中的购物车   这里默认购物车中商品全部提交，没有对复选框进行使用
        jedis.del("buyerCart:"+ username);
        //联系 hash 指定key 进行删除
    }

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ColorDao colorDao;
    public Sku selectSkuById(Long id){
        //sku对象
        Sku sku = skuDao.selectByPrimaryKey(id);
        //商品对象
        sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
        //Collre对象
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        return sku;
    }

    //从Redis购物车中奖商品取出
    public BuyerCar selectBuyerCartFromRedis(String username){
        BuyerCar buyerCar=new BuyerCar();
        Map<String, String> map = jedis.hgetAll("buyerCart:" + username);
        if(null != map){
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for(Map.Entry<String, String> entry:entries){
                //将Redis中购物车商品取出
                Sku sku=new Sku();
                //skuId
                sku.setId(Long.parseLong(entry.getKey()));
                BuyerItem buyerItem=new BuyerItem();
                buyerItem.setSku(sku);
                //商品数量
                buyerItem.setAmount(Integer.parseInt(entry.getValue()));
                //追加商品到购物车
                buyerCar.addBuyerItem(buyerItem);
            }
        }
        return buyerCar;
    }


}

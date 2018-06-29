package com.lyb.core.service.product;

import com.lyb.core.bean.BuyerCar;
import com.lyb.core.bean.BuyerItem;
import com.lyb.core.bean.product.Color;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.bean.product.SkuQuery;
import com.lyb.core.bean.user.Buyer;
import com.lyb.core.dao.product.ColorDao;
import com.lyb.core.dao.product.ProductDao;
import com.lyb.core.dao.product.SkuDao;
import org.jboss.netty.util.internal.ReusableIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 库存管理
 * 库存查询
 * Created by lin on 2018/3/30.
 */
@Service("skuService")
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;
    //通过商品id查询库存结果集
    public List<Sku> selectSkuListByProductId(Long productId){
        SkuQuery skuQuery=new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(productId);
        List<Sku> skus=skuDao.selectByExample(skuQuery);
        for(Sku sku:skus){
            //由于只有三种颜色这里中向数据库请求三次sql  一级缓存
            sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }

    @Autowired
    private ProductDao productDao;
    //通过skuid保修改库存
    public void updateSkuById(Sku sku){
        skuDao.updateByPrimaryKeySelective(sku);
    }

    public Sku selectSkuById(Long id){

        //sku对象
        Sku sku = skuDao.selectByPrimaryKey(id);
        //商品对象
        sku.setProduct(productDao.selectByPrimaryKey(sku.getProductId()));
        //Collre对象
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        return sku;
    }

    @Autowired
    private Jedis jedis;
    //保存购物车中商品到redis中购物车
    public void insertBuyerCartToRedis(BuyerCar buyerCar,String userName){ //用到Dubbo 所以BuyerCar和 BuyerItem类都需要就行序列化
        List<BuyerItem> items = buyerCar.getItems();
        if(items.size()>0){
            for(BuyerItem buyerItem:items){
                //保存到redis中 用hash容器  先判断商品是否已经存在用户redis购物车中
                if(jedis.hexists("buyerCart:"+userName,String.valueOf(buyerItem.getSku().getId()))){
                    //已经存在直接追加就行hincrBy
                    jedis.hincrBy("buyerCart:"+userName,String.valueOf(buyerItem.getSku().getId()),buyerItem.getAmount());
                }else { //redis中不存在，直接添加
                    jedis.hset("buyerCart:"+userName,String.valueOf(buyerItem.getSku().getId()),String.valueOf(buyerItem.getAmount()));
                }
            }
        }
    }

    //从Redis购物车中将商品取出
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
package com.lyb.core.service;

import com.lyb.core.bean.product.Product;
import com.lyb.core.bean.product.ProductQuery;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.bean.product.SkuQuery;
import com.lyb.core.dao.product.ColorDao;
import com.lyb.core.dao.product.ProductDao;
import com.lyb.core.dao.product.SkuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论
 * 晒单
 *广告
 * 静态化
 * Created by lin on 2018/4/13.
 */
@Service("cmsService")
public class CmsServiceImpl implements CmsService{

    @Autowired
    private ProductDao productDao;
    @Autowired
    private SkuDao skuDao;
    @Autowired
    private ColorDao colorDao;

    //查询商品
    public Product selectProductById(Long productId){
        return productDao.selectByPrimaryKey(productId);
    }

    //查询Sku结果集  （包括颜色）  有货的查，没货的不查
    public List<Sku> selectSkuListByProductId(Long productId){
        SkuQuery skuQuery=new SkuQuery();
        skuQuery.createCriteria().andProductIdEqualTo(productId).andStockGreaterThan(0); // 查询库存Stock大于0的
        List<Sku> skus = skuDao.selectByExample(skuQuery);

        System.out.println("库存量："+skus.size());
        for (Sku sku:skus) {
        sku.setColor(colorDao.selectByPrimaryKey(sku.getColorId()));
        }
        return skus;
    }
}

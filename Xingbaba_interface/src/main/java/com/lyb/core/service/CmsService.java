package com.lyb.core.service;

import com.lyb.core.bean.product.Product;
import com.lyb.core.bean.product.Sku;

import java.util.List;

/**
 * Created by lin on 2018/4/13.
 */
public interface CmsService {

    //查询商品
    public Product selectProductById(Long productId);
    //查询Sku结果集  （包括颜色）  有货的查，没货的不查
    public List<Sku> selectSkuListByProductId(Long productId);
}

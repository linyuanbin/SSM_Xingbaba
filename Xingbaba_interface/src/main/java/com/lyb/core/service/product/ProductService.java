package com.lyb.core.service.product;


import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Color;
import com.lyb.core.bean.product.Product;

import java.util.List;

/**
 * Created by lin on 2018/3/25.
 */
public interface ProductService {
    public Pagination selectPaginationByQuery(Integer pageNo, String name, Long brandId, Boolean isShow);
    //加载颜色
    public List<Color> selectColorList();
    public void insertProduct(Product product);
    //商品上架 变更商品状状态
    public void isShow(Long[] ids);
}

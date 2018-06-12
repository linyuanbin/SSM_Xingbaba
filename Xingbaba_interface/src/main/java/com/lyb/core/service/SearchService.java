package com.lyb.core.service;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Product;

import java.util.List;

/**
 * Created by lin on 2018/4/6.
 */
public interface SearchService {

    public Pagination searchPaginationByQuery(Integer pageNo,String keywore,Long brandId,String price) throws Exception ;
    //上架是时保存商品到solr
    public void insertProductToSolr(Long id);
}

package com.lyb.core.service;

import com.lyb.core.bean.TestTb;
import com.lyb.core.bean.product.Product;
import com.lyb.core.bean.product.ProductQuery;
import com.lyb.core.dao.TestTbDao;
import com.lyb.core.dao.product.ProductDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

/**
 * Created by lin on 2018/3/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class TestProduct {

@Autowired
    private TestTbDao testTbDao;

    @Autowired
    private ProductDao productDao;
    @Test
    public void test()throws Exception{

        Product product = productDao.selectByPrimaryKey(441L);
        System.out.println("-------------------------------------");
        System.out.println(product);

    }
    @Test
    public void add()throws Exception{

        ProductQuery productQuery=new ProductQuery();
        //productQuery.createCriteria().andBrandIdEqualTo(4L).andNameLike("%好莱坞%");
        //分页
        productQuery.setPageNo(2);
        productQuery.setPageSize(10);
         //排序查询  （排序优先）
        productQuery.setOrderByClause("id desc");
        //指定字段查询
        productQuery.setFields("id,brand_id");
        List<Product> products = productDao.selectByExample(productQuery);
        for(Product pic:products){
            System.err.println(pic);
        }


    }

}

package com.lyb.core.service.product;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Brand;
import com.lyb.core.bean.product.BrandQuery;

import com.lyb.core.bean.product.Product;
import com.lyb.core.dao.product.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 品牌管理
 * Created by lin on 2018/3/19.
 */
@Service("brandService")
@Transactional
public class BrandServiceImpl implements BrandService{

    @Autowired
    private BrandDao brandDao;

    //查询分页对象
    public Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo){
        BrandQuery brandQuery=new BrandQuery();
        //设置当前页 从1开始
        brandQuery.setPageNo(Pagination.cpn(pageNo)); //Pagination.cpn()是当pageNo为空时就将pageNo设置为1
        //设置每页数据条数
        brandQuery.setPageSize(3);
        StringBuilder params=new StringBuilder();
        if(null != name){
            brandQuery.setName(name);
            params.append("name=").append(name);
        }
        if(null != isDisplay){
            brandQuery.setIsDisplay(isDisplay);
            params.append("&isDisplay=").append(isDisplay);
        }else{
            brandQuery.setIsDisplay(1);
            params.append("&isDisplay=").append(1);
        }

        //Pagination结果集就是分页的结果
        Pagination pagination= new Pagination(
                brandQuery.getPageNo(),
                brandQuery.getPageSize(),
                brandDao.selectCount(brandQuery)
        );

        //设置结果集
        pagination.setList(brandDao.selectBrandListByQuery(brandQuery));

        //分页展示   pagination工具类提供的自动分页操作
        String url="/brand/list.do";
        pagination.pageView(url,params.toString());

    return pagination;
    }

    @Override
    public Brand selectBrandById(Long id) {
        return brandDao.selectBrandById(id);
    }

    @Autowired
    private Jedis jedis;
    //品牌修改
    @Override
    public void updateBrandById(Brand brand) {
        //修改Redis
        jedis.hset("brand",String.valueOf(brand.getId()),brand.getName());
        brandDao.updateBrandById(brand);  //修改mysql数据
    }
    //查询 从Redis中查
    @Override
    public List<Brand> selectBrandListFromRedis(){
        List<Brand> brands=new ArrayList<Brand>();
        Map<String, String> map = jedis.hgetAll("brand");
        Set<Map.Entry<String, String>> entries = map.entrySet();
        for(Map.Entry<String, String> entry:entries){
            Brand brand=new Brand();
            brand.setId(Long.parseLong(entry.getKey()));
            brand.setName(entry.getValue());
            brands.add(brand);
        }
        return brands;
    }

    @Override
    public void deletes(Long[] ids) {
        brandDao.deletes(ids);
    }

    @Override
    public List<Brand> selectBrandListByQuery(Integer isDisplay) {
        BrandQuery brandQuery=new BrandQuery();
        brandQuery.setIsDisplay(isDisplay);
        return brandDao.selectBrandListByQuery(brandQuery);
    }

    //添加品牌
    public void insertBrand(Brand brand){
        brandDao.insertBrand(brand);
    }



}

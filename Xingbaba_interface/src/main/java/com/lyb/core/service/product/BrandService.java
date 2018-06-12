package com.lyb.core.service.product;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Brand;
import com.lyb.core.bean.product.BrandQuery;

import java.util.List;

/**
 * Created by 言溪 on 2018/3/19.
 */
public interface BrandService {
    //查询结果集
    public Pagination selectPaginationByQuery(String name, Integer isDisplay, Integer pageNo);
    //通过id查询
    public Brand selectBrandById(Long id);
    //通过id修改
    public void updateBrandById(Brand brand);
    //批量删除
    public void deletes(Long[] ids);
    //查询结果集
    public List<Brand> selectBrandListByQuery(Integer isDisplay);
    //从Redis中查询
    public List<Brand> selectBrandListFromRedis();

    //添加品牌
    public void insertBrand(Brand brand);


}

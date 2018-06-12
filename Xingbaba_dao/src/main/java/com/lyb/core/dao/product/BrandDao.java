package com.lyb.core.dao.product;

import com.lyb.core.bean.product.Brand;
import com.lyb.core.bean.product.BrandQuery;

import java.util.List;


/**
 * Created by lin on 2018/3/19.
 */
public interface BrandDao {

    //查询结果集
    public List<Brand> selectBrandListByQuery(BrandQuery brandQuery);
    //查询总条数
    public Integer selectCount(BrandQuery brandQuery);
    //通过id查询
    public Brand selectBrandById(Long id);

    //通过id修改
    public void updateBrandById(Brand brand);
    //批量删除
    public void deletes(Long[] ids);

    //添加
    public void insertBrand(Brand brand);


}

package com.lyb.core.controller;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Brand;
import com.lyb.core.bean.product.Color;
import com.lyb.core.bean.product.Product;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.service.CmsService;
import com.lyb.core.service.SearchService;
import com.lyb.core.service.product.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * 前台商品
 * Created by lin on 2018/4/3.
 */
@Controller
public class ProductController{

    @RequestMapping("/index.do")
    public String index(){
        return "index";
    }

    @Autowired
    private SearchService searchService;
    @Autowired
    private BrandService brandService;
    @RequestMapping("/search")
    public String search(Integer pageNo,String keyword,Long brandId,String price, Model model) throws Exception {
        //获取品牌名称结果集 从Redis中获得
        List<Brand> brands = brandService.selectBrandListFromRedis();
        model.addAttribute("brands",brands);
        model.addAttribute("brandId",brandId);
        model.addAttribute("price",price);

        //已选条件容器 Map
        Map<String,String> map=new HashMap<String, String>();
        //品牌
        if(null != brandId){
            for(Brand brand:brands){
                if(brandId == brand.getId()){
                    map.put("品牌",brand.getName());
                    break;
                }
            }
        }
        //价格
        if(null != price){
            if(price.contains("-")){ // 0-99
                map.put("价格",price);
            }else {
                map.put("价格",price+"以上");  //1600以上
            }
        }
        model.addAttribute("map",map);
        Pagination pagination = searchService.searchPaginationByQuery(pageNo,keyword,brandId,price);
        model.addAttribute("pagination",pagination);
        /*if(null!=keyword)
        {
          model.addAttribute("keyword",keyword);
        }*/
        model.addAttribute("keyword",keyword);
        return "search";
    }

    @Autowired
    private CmsService cmsService;
    //去商品详情界面
    @RequestMapping("/product/detail")
    public String detail(Long id,Model model){
        //商品
        Product product = cmsService.selectProductById(id);
        //sku集
        List<Sku> skus = cmsService.selectSkuListByProductId(id);

        //遍历一次  颜色相同不要
        Set<Color> colors=new HashSet<Color>();  //Set中存放的是对象，要去重需要重写Color的equals和hashCode方法，通过比较对象的id进行去重
        for(Sku sku:skus){
            colors.add(sku.getColor());
        }

        model.addAttribute("product",product); //商品对象
        model.addAttribute("skus",skus); //sku对象集 包含color对象
        model.addAttribute("colors",colors);
        return "product";
    }

}

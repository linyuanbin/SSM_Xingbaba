package com.lyb.core.controller;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Brand;
import com.lyb.core.bean.product.Color;
import com.lyb.core.bean.product.Product;
import com.lyb.core.service.product.BrandService;
import com.lyb.core.service.product.ProductService;
import com.sun.javafx.sg.PGShape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.PrintWriter;
import java.util.List;

/**商品管理
 * 列表
 * 添加
 * 上架
 * Created by lin on 2018/3/25.
 */

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private BrandService brandService;
    //查询
    @RequestMapping(value = "/product/list.do")
    public String list(Integer pageNo, String name, Long brandId, Boolean isShow, Model model){ //注意这里的Boolean是包装类，不能是小写的

        //品牌结果集
        List<Brand> brands = brandService.selectBrandListByQuery(1);//查询isDisplay=1的
        model.addAttribute("brands",brands);
        Pagination pagination = productService.selectPaginationByQuery(pageNo, name, brandId, isShow);
        model.addAttribute("pagination",pagination);
        model.addAttribute("name",name);
        model.addAttribute("brandId",brandId);
        if(null != isShow){
            model.addAttribute("isShow",isShow);
        }else {
            model.addAttribute("isShow",false);
        }
        return "product/list";
    }

    //去添加页面
    @RequestMapping(value = "/product/toAdd.do")
    public String toAdd(Model model){
        List<Color> colors = productService.selectColorList();//颜色结果集
        model.addAttribute("colors",colors);
        //品牌结果集
        List<Brand> brands = brandService.selectBrandListByQuery(1);//查询isDisplay=1的
        model.addAttribute("brands",brands);
        return "product/add";
    }

    //商品保存
    @RequestMapping(value = "/product/add.do")
    public String add(Product product){
        productService.insertProduct(product);
        return "redirect:/product/list.do";
    }

    //商品批量上架  变更商品状态
    @RequestMapping(value = "/product/isShow.do")
    public String isShow(Long[] ids){
        productService.isShow(ids);
        return "forward:/product/list.do";
    }

}

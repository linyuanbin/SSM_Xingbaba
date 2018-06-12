package com.lyb.core.controller;

import cn.itcast.common.page.Pagination;
import com.lyb.core.bean.product.Brand;
import com.lyb.core.service.product.BrandService;

import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lin on 2018/3/19.
 */
@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    //查询
    @RequestMapping(value="/brand/list.do")
    public String list(String name, Integer isDisplay, Integer pageNo, Model model){

        Pagination pagination=brandService.selectPaginationByQuery(name,isDisplay,pageNo);
        model.addAttribute("pagination",pagination);
        model.addAttribute("name",name);
        if(null != isDisplay){
            model.addAttribute("isDisplay",isDisplay);
        }else{
            model.addAttribute("isDisplay",1);
        }
        return "brand/list";
    }

    @RequestMapping(value="/brand/list2.do")
    public String list2(String name, Integer isDisplay, Integer pageNo, Model model){
        name="";pageNo=0;
        isDisplay=1;
        Pagination pagination=brandService.selectPaginationByQuery(name,isDisplay,pageNo);
        model.addAttribute("pagination",pagination);
        model.addAttribute("name",name);
        if(null != isDisplay){
            model.addAttribute("isDisplay",isDisplay);
        }else{
            model.addAttribute("isDisplay",1);
        }
        return "brand/list";
    }

    //去修改页面
    @RequestMapping(value="/brand/toEdit.do")
    public String toEdit(Long id, Model model){
        Brand brand=brandService.selectBrandById(id);
        model.addAttribute("brand",brand);
        return "brand/edit";
    }

    //提交修改
    @RequestMapping(value="/brand/edit.do")
    public String edit(Brand brand){
        brandService.updateBrandById(brand);
        return "redirect:/brand/list.do"; //重定向
    }

    //去添加界面
    @RequestMapping(value="/brand/toAdd.do")
    public String toAdd(Model model){
        return "/brand/add";
    }

    //去添加
    @RequestMapping(value="/brand/add.do")
    public String add(Brand brand,Model model){
        System.out.println(brand.toString());
        brandService.insertBrand(brand);
        return "forward:/brand/list2.do";
    }


    //批量删除
    @RequestMapping(value="/brand/deletes.do")
    public String deletes(Long[] ids){//,String name,Integer isDisplay,Integer pageNo
       // System.out.println(ids.toString()+" name="+name+" isDisplay="+isDisplay+" pageNo="+pageNo);
        brandService.deletes(ids);
        return "forward:/brand/list.do"; //forward直接转发到public String list，括号中的name、isDisplay和pageNo可以省略不写,参数值也会传到brand/list.do
    }


}

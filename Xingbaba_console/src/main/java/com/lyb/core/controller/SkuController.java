package com.lyb.core.controller;

import com.lyb.core.bean.product.Sku;
import com.lyb.core.service.product.SkuService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 库存管理
 * 去库存页面
 * js 修改
 * 保存
 * Created by lin on 2018/3/30.
 */
@Controller
public class SkuController {

    @Autowired
    private SkuService skuService;

    //去库存页面
    @RequestMapping(value = "/sku/list.do")
    public String list(Long productId,Model model){
        List<Sku> skus=skuService.selectSkuListByProductId(productId);
        model.addAttribute("skus",skus);
        return "sku/list";
    }

    //ajax 异步保存数据
    @RequestMapping(value = "/sku/addSku.do")
    public void addSku(Sku sku, HttpServletResponse response) throws IOException {

        skuService.updateSkuById(sku);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("messages","保存成功！");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(jsonObject.toString());
    }

}

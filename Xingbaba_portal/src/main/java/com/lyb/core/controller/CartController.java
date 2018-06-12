package com.lyb.core.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyb.common.utils.RequestUtils;
import com.lyb.common.web.Constants;
import com.lyb.core.bean.BuyerCar;
import com.lyb.core.bean.BuyerItem;
import com.lyb.core.bean.order.Order;
import com.lyb.core.bean.product.Sku;
import com.lyb.core.bean.user.Buyer;
import com.lyb.core.service.product.SkuService;
import com.lyb.core.service.user.BuyerService;
import com.lyb.core.service.user.SessionProvider;
import com.sun.org.apache.bcel.internal.generic.IREM;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * 购物车
 * 去购物车界面
 * 添加商品到购物车
 * 删除
 * Created by lin on 2018/4/21.
 */
@Controller
public class CartController {

    @Autowired
    private SessionProvider sessionProvider;
    @RequestMapping("/addCart")
    public String addCart(Long skuId, Integer amount, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        //ObjectMapper 实现Json和对象相互转换
        ObjectMapper om=new ObjectMapper();
        //设置转换的Json字符串中不包含Null 的部分属性
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        //声明购物车对象
        BuyerCar buyerCar=null;

        //1.从Request中取出Cookie
        Cookie[] cookies = request.getCookies();
        //2.判断cookie中是否有购物车
        if(null != cookies && cookies.length > 0){
            for(Cookie cookie:cookies){
                //获取购物车对象
                if(Constants.BUYER_CART.equals(cookie.getName())){
                    buyerCar=om.readValue(cookie.getValue(),BuyerCar.class); //将购物车对象Json数据转换成购物车对象
                    break;
                }
            }
        }

        //3.有
        //4.没有 创建购物车对象
        if(null == buyerCar) {
            buyerCar = new BuyerCar();
        }
        //5.将当前商品追加cookie中的商品到购物车 不来等没登陆都需要
        Sku sku=new Sku();
        //skuId
        sku.setId(skuId);
        BuyerItem buyerItem=new BuyerItem();
        buyerItem.setSku(sku);
        //商品数量
        buyerItem.setAmount(amount);
        buyerCar.addBuyerItem(buyerItem);
        StringWriter sw=new StringWriter();
        om.writeValue(sw,buyerCar); //讲对象通过流方式转成Json格式

        String userName = sessionProvider.getAttributeForUserName(RequestUtils.getCSESSIONID(request, response));
        //判断用户是否登录
        if(null != userName){//登录
                //3.有 把购物车商品添加到Redis的购物车中，清理之前Cookie
                skuService.insertBuyerCartToRedis(buyerCar,userName);
                //清理cookie
                Cookie cookie=new Cookie(Constants.BUYER_CART,null);
                cookie.setMaxAge(0);//立即执行
                cookie.setPath("/");
                response.addCookie(cookie);

            //4.没有
            //5.直接添加当前商品到redis中的购物车中

        }else{ //没登录
            //6.创建新cookie 将购物车放进去
            Cookie cookie=new Cookie(Constants.BUYER_CART,sw.toString());
            //设置时间  保存一天
            cookie.setMaxAge(60*60*24);
            //设置路径
            cookie.setPath("/");
            //设置域 待完善

            //7.保存cookie到浏览器
            response.addCookie(cookie);
        }
        return "redirect:/toCart";
    }

    @Autowired
    private SkuService skuService;
    @RequestMapping("/toCart")
    public String toCart(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

        //ObjectMapper 实现Json和对象相互转换
        ObjectMapper om=new ObjectMapper();
        //设置转换的Json字符串中不包含Null 的部分属性
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //声明购物车对象
        BuyerCar buyerCart=null;
        //1.从Request中取出Cookie
        Cookie[] cookies = request.getCookies();
        //2.判断cookie中是否有购物车
        if(null != cookies && cookies.length > 0){
            for(Cookie cookie:cookies){
                //3.有 获取购物车对象
                if(Constants.BUYER_CART.equals(cookie.getName())){
                    buyerCart=om.readValue(cookie.getValue(),BuyerCar.class); //将购物车对象Json数据转换成购物车对象
                    break;
                }
            }
        }

        //判断是否登录
        String userName = sessionProvider.getAttributeForUserName(RequestUtils.getCSESSIONID(request, response));
        if(null != userName){
            if(null != buyerCart){
                //已经登录
                //2.把购物车保存到redis中 清理cookie
                skuService.insertBuyerCartToRedis(buyerCart,userName);
                //清理之前cookie
                Cookie cookie=new Cookie(Constants.BUYER_CART,null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
            //3.从Redis中取出所有购物车
            buyerCart = skuService.selectBuyerCartFromRedis(userName);
        }

        if(null != buyerCart) {
            //2.有 购物车数量 及 SKUID
            //将购物车转满  通过skuid奖购物车中需要的查询的 sku、商品、颜色 对象通过service层查询n
            List<BuyerItem> items = buyerCart.getItems();
            for(BuyerItem buyerItem:items){
                //将购物车装满
                buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
            }
        }

        //3.没有
        //4.回显数据 model
        model.addAttribute("buyerCart",buyerCart);
        //5.回到购物车界面
        return "cart";
    }

    //结算
    @RequestMapping(value = "/buyer/trueBuy")  //由于设定进入结算界面用户必须是登录的，所有/buyer/这个路径请求只能是登录用户可以过，其余的拦截，配置springMvc拦截器
    public String trueBuy(Long[] skuIds, Model model,HttpServletRequest request, HttpServletResponse response){
        //进入方法，说名用户已经登录，没有被拦截
        //1.购物车中必须有商品
        //从Redis中取出所有购物车
        String userName = sessionProvider.getAttributeForUserName(RequestUtils.getCSESSIONID(request, response));
        BuyerCar buyerCart = skuService.selectBuyerCartFromRedis(userName);
        List<BuyerItem> items = buyerCart.getItems();
        if(items.size()>0){
        //2.购物车中必须有库存
            boolean flag=false;
                for(BuyerItem buyerItem:items){
                    //将购物车装满
                    buyerItem.setSku(skuService.selectSkuById(buyerItem.getSku().getId()));
                    //it.getAmount() 和  库存比较
                    if(buyerItem.getAmount() > buyerItem.getSku().getStock()){
                        //无货   购买数量大于库存数量
                        buyerItem.setHave(false);
                        flag=true;
                    }
                }
            if(flag){ //至少有一个商品无货
                //视图 只要有一个没货不能直接进入订单界面
                model.addAttribute("buyerCart",buyerCart);
                return "cart";
            }
        }else {//购物车中没有商品
            //刷新购物车界面
            return "redirect:/toCart";
        }
        //视图 如果都有货，直接进入下一个订单界面
        return "order";
    }

    @Autowired
    private BuyerService buyerService;
    //提交订单
    @RequestMapping(value = "/buyer/submitOrder") //进入这里的一定是已经登录的
    public String submitOrder(Order order,Model model, HttpServletRequest request, HttpServletResponse response){
        String userName = sessionProvider.getAttributeForUserName(RequestUtils.getCSESSIONID(request, response));
        buyerService.insertOrder(order,userName);
        return "success";
    }

    }

package com.lyb.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyb.core.bean.user.Buyer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 购物车
 * Created by lin on 2018/4/21.
 */
public class BuyerCar implements Serializable{

    //1. 商品结果集
    private List<BuyerItem> items=new ArrayList<BuyerItem>();

    public void addBuyerItem(BuyerItem buyerItem){
        //加入之前进行判断是否有相同商品
        if(items.contains(buyerItem)){ //存在   contains 底层是equals 比较的是变量的地址，显然这里需要比较SkuId,所以需要重写BuyerItem的HashCode和Equals方法
            for(BuyerItem bi:items){
                if(bi.equals(buyerItem)){
                    bi.setAmount(bi.getAmount()+buyerItem.getAmount()); //只需要数量增加就行
                }
            }
        }else{//不存在 ，直接添加到购物车
            items.add(buyerItem);
        }
    }

    public List<BuyerItem> getItems() {
        return items;
    }

    public void setItems(List<BuyerItem> items) {
        this.items = items;
    }

//2.小计 （商品数量、商品金额、运费、总计）
    /*
    * 注意事项：
    *           由于下面添加了几个方法，使得BuyerCar购物车不再是一个标准的JavaBean,因此在转换成Json字符串是会出现错误
    *           由于使用的是 com.fasterxml.jackson 提供的ObjectMapper对象进行Json和对象的互转
    *           因此这里用   @JsonIgnore标签进行忽略，使得在转换Json时不对这些方法进行操作，这样才不会报错！
    * */
    //计算商品总量
    @JsonIgnore
    public Integer getProductAmount(){
        Integer result=0;
        for (BuyerItem item:items) {
            result+=item.getAmount();
        }
        return result;
    }


    //计算商品费用
    @JsonIgnore
    public Float getProductPrice(){
        Float result=0f;
        for (BuyerItem item:items) {
            result+=item.getAmount()*item.getSku().getPrice();
        }
        return result;
    }

    //判断运费
    @JsonIgnore
    public Float getFee(){
        Float result=0f;
        if(getProductPrice()< 88){ //少于88 收取5元运费
            return 5f;
        }
        return result;
    }
    //计算总金额
    @JsonIgnore
    public Float getTotalPrice(){
        return getProductPrice()+getFee();
    }
}

package com.lyb.core.bean;

import com.lyb.core.bean.product.Sku;

import java.io.Serializable;

/**
 * 购物车中对象
 * Created by lin on 2018/4/21.
 */
public class BuyerItem implements Serializable{


    /**
     *
     */
    private static final long serialVersionUID = 1L;

    //1:SKUID   Sku对象里面有个自己的ID
    private Sku sku;

    //2：Boolean 是否有货;
    private Boolean have = true;

    //3:数量
    private Integer amount = 1;

    public Sku getSku() {
        return sku;
    }

    public void setSku(Sku sku) {
        this.sku = sku;
    }

    public Boolean getHave() {
        return have;
    }

    public void setHave(Boolean have) {
        this.have = have;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "BuyerItem{" +
                "sku=" + sku +
                ", have=" + have +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        BuyerItem buyerItem = (BuyerItem) object;

        return sku != null ? sku.getId().equals(buyerItem.sku.getId()) : buyerItem.sku == null;  //修改部分： sku.getId().equals(buyerItem.sku.getId()) 比较skuID

    }

    @Override
    public int hashCode() {
        return sku != null ? sku.hashCode() : 0;
    }
}


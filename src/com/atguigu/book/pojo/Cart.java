package com.atguigu.book.pojo;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 购物车类，描述的是一整个购物车
 *
 * @author Neo
 * @version 1.0
 */

public class Cart {
    private Map<Integer, CartItem> cartItemMap;     //购物车中购物车项的集合 , 这个Map集合中的key是Book的id
    private Double totalMoney;                     //购物车的总金额
    private Integer totalCount;                    //购物车中购物项的数量
    private Integer totalBookCount;                //购物车中书本的总数量，而不是购物车项的总数量

    public Map<Integer, CartItem> getCartItemMap() {
        return cartItemMap;
    }

    public void setCartItemMap(Map<Integer, CartItem> cartItemMap) {
        this.cartItemMap = cartItemMap;
    }

    public Double getTotalMoney() {
        totalMoney = 0.0;
        if (cartItemMap != null && cartItemMap.size() > 0) {
            Set<Map.Entry<Integer, CartItem>> entries = cartItemMap.entrySet();
            for (Map.Entry<Integer, CartItem> cartItemEntry : entries) {
                CartItem cartItem = cartItemEntry.getValue();
                //用BigDecimal包装单价和数量
                BigDecimal price = new BigDecimal(cartItem.getBook().getPrice());
                BigDecimal buyCount = new BigDecimal(cartItem.getBuyCount());
                //单价 * 数量 得到总价
                BigDecimal totalPrice = price.multiply(buyCount);
                BigDecimal total = new BigDecimal(totalMoney);
                //总价累加
                total = total.add(totalPrice);
                totalMoney = total.doubleValue();
            }

        }
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getTotalCount() {
        totalCount = 0;
        if (cartItemMap != null && cartItemMap.size() > 0) {
            totalCount = cartItemMap.size();
        }
        return totalCount;
    }


    public Integer getTotalBookCount() {
        totalBookCount = 0;
        if (cartItemMap != null && cartItemMap.size() > 0) {
            Collection<CartItem> values = cartItemMap.values();
            for (CartItem cartItem : values) {
                totalBookCount = totalBookCount + cartItem.getBuyCount();
            }
        }
        return totalBookCount;
    }

}

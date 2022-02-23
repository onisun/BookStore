package com.atguigu.book.dao;

import com.atguigu.book.pojo.CartItem;
import com.atguigu.book.pojo.User;

import java.util.List;

/**
 * @author Neo
 * @version 1.0
 */
public interface CartItemDAO {
    //新增购物车项
    void addCartItem(CartItem cartItem);
    //修改特定的购物车项
    void updateCartItem(CartItem cartItem);

    //获取特定用户的所有购物车项
    List<CartItem> getCartItemList(User user);

    //删除
    void delCartItem (CartItem cartItem);



}

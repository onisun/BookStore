package com.atguigu.book.service;

import com.atguigu.book.pojo.Cart;
import com.atguigu.book.pojo.CartItem;
import com.atguigu.book.pojo.User;

import java.util.List;

/**
 * @author Neo
 * @version 1.0
 */
public interface CartItemService {
    //新增购物车项
    void addCartItem(CartItem cartItem);
    //修改特定的购物车项
    void updateCartItem(CartItem cartItem);

    void addOrUpdateCartItem(CartItem cartItem, Cart cart);

    //加载特定用户的购物车信息
    Cart getCart(User user);

    //根据bookId获取CartItem

    //获取特定用户的所有购物车项
    List<CartItem> getCartItemList(User user);

    //删除
    void delCartItem (CartItem cartItem);
}

package com.atguigu.book.controller;

import com.atguigu.book.pojo.Book;
import com.atguigu.book.pojo.CartItem;
import com.atguigu.book.pojo.User;
import com.atguigu.book.service.CartItemService;
import com.atguigu.book.pojo.Cart;
import com.google.gson.Gson;

import javax.servlet.http.HttpSession;


/**
 * @author Neo
 * @version 1.0
 */
public class CartController {

    private CartItemService cartItemService;

    public String addCart(Integer bookId, HttpSession session) {
        //如果当前用户的购物车中没有改item，则在购物车添加该item
        //如果有该item，则将该item的数量+1
        User user = (User) session.getAttribute("currUser");
        CartItem cartItem = new CartItem(new Book(bookId), 1, user);

        cartItemService.addOrUpdateCartItem(cartItem, user.getCart());
        return "redirect:cart.do";
    }


    public String index(HttpSession session){
        User user = (User) session.getAttribute("currUser");
        Cart cart = cartItemService.getCart(user);
        user.setCart(cart);

        session.setAttribute("currUser",user);
        return "cart/cart";
    }

    public String editCart(Integer cartItemId,Integer buyCount){
        if (buyCount < 1)
            buyCount = 1;
        cartItemService.updateCartItem(new CartItem(cartItemId,buyCount));
        return "";
    }

    public String cartInfo(HttpSession session){
        User user = (User) session.getAttribute("currUser");
        Cart cart = cartItemService.getCart(user);
        Gson gson = new Gson();
        String cartJsonStr = gson.toJson(cart);
        return "json:"+cartJsonStr;

    }

}

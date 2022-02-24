package com.atguigu.book.controller;

import com.atguigu.book.pojo.Cart;
import com.atguigu.book.pojo.User;
import com.atguigu.book.service.CartItemService;
import com.atguigu.book.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * @author Neo
 * @version 1.0
 */
public class UserController {
    private UserService userService;
    private CartItemService cartItemService;

    public String login(String uname, String pwd, HttpSession session){
        User user = userService.login(uname, pwd);
        if (user != null){
            Cart cart = cartItemService.getCart(user);
            user.setCart(cart);
            session.setAttribute("currUser",user);
            return "redirect:book.do";
        }
        return "user/login";

    }

    public String regist(String uname,String pwd,String email,String verifyCode,HttpSession session){
        Object kaptcha_session_key = session.getAttribute("KAPTCHA_SESSION_KEY");
        if (verifyCode == null || !verifyCode.equals(kaptcha_session_key)){
            session.setAttribute("registReturn","-1");
            return "user/regist_success";
        }
        //注册
        userService.addUser(new User(uname,pwd,email,0));
        session.setAttribute("registReturn","1");
        return "user/regist_success";

    }
}

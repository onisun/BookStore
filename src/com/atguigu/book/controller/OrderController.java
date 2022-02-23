package com.atguigu.book.controller;

import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.User;
import com.atguigu.book.service.OrderService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Neo
 * @version 1.0
 */
public class OrderController {

    private OrderService orderService;

    public String checkout(HttpSession session){
        //结账：
        //1.向订单表 （t_order）中新增一条记录
        //2.向订单详情表中(t_order_item)中新增7条记录
        //3.清空购物车中的记录
        User user = (User)session.getAttribute("currUser");

        String orderNo = UUID.randomUUID().toString();
        Date orderDate = new Date();
        Double orderMoney =  user.getCart().getTotalMoney();

        OrderBean orderBean = new OrderBean(orderNo,orderDate,user,orderMoney,0);
        orderService.addOrderBean(orderBean);
        return "index";
    }

    public String index(HttpSession session){
        User user = (User)session.getAttribute("currUser");
        List<OrderBean> orderList = orderService.getOrderList(user);
        user.setOrderBeanList(orderList);
        session.setAttribute("currUser",user);
        return "order/order";
    }
}

package com.atguigu.book.service.impl;

import com.atguigu.book.dao.OrderDAO;
import com.atguigu.book.pojo.CartItem;
import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.OrderItem;
import com.atguigu.book.pojo.User;
import com.atguigu.book.service.CartItemService;
import com.atguigu.book.service.OrderItemService;
import com.atguigu.book.service.OrderService;

import java.util.List;
import java.util.Map;

/**
 * @author Neo
 * @version 1.0
 */
public class OrderServiceImpl implements OrderService {
    private OrderDAO orderDAO;
    private OrderItemService orderItemService;
    private CartItemService cartItemService;
    @Override
    public void addOrderBean(OrderBean orderBean) {
        //1.向订单表 （t_order）中新增一条记录
        //2.向订单详情表中(t_order_item)中新增7条记录
        //3.清空购物车中的记录

        //1.
        orderDAO.addOrderBean(orderBean);

        //2.
        User currUser = orderBean.getOrderUser();
        Map<Integer, CartItem> cartItemMap = currUser.getCart().getCartItemMap();
        for (CartItem cartItem : cartItemMap.values()){
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setBuyCount(cartItem.getBuyCount());
            orderItem.setOrderBean(orderBean);

            orderItemService.addOrderItem(orderItem);
        }

        //3.
        for (CartItem cartItem : cartItemMap.values()){
            cartItemService.delCartItem(cartItem);
        }

    }

    @Override
    public List<OrderBean> getOrderList(User user) {
        List<OrderBean> orderList = orderDAO.getOrderList(user);
        for (OrderBean orderBean : orderList) {
            Integer orderTotalBookCount = orderDAO.getOrderTotalBookCount(orderBean);
            orderBean.setTotalBookCount(orderTotalBookCount);
        }
        return orderList;
    }

    @Override
    public Integer getOrderTotalBookCount(OrderBean orderBean) {
        return orderDAO.getOrderTotalBookCount(orderBean);
    }
}

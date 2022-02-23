package com.atguigu.book.service;

import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.User;

import java.util.List;

/**
 * @author Neo
 * @version 1.0
 */
public interface OrderService {
    //向订单表 （t_order）中新增一条记录
    void addOrderBean(OrderBean orderBean);

    //获取某个用户的所有订单列表
    List<OrderBean> getOrderList(User user);

    Integer getOrderTotalBookCount(OrderBean orderBean);
}

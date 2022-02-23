package com.atguigu.book.dao;
import com.atguigu.book.pojo.OrderBean;
import com.atguigu.book.pojo.User;

import java.util.List;

/**
 * @author Neo
 * @version 1.0
 */
public interface OrderDAO {
    //向订单表 （t_order）中新增一条记录
    void addOrderBean(OrderBean orderBean);

    //获取订单列表
    List<OrderBean> getOrderList(User user);

    Integer getOrderTotalBookCount(OrderBean orderBean);
}

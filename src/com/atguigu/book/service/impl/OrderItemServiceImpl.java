package com.atguigu.book.service.impl;

import com.atguigu.book.dao.OrderItemDAO;
import com.atguigu.book.pojo.OrderItem;
import com.atguigu.book.service.OrderItemService;

/**
 * @author Neo
 * @version 1.0
 */
public class OrderItemServiceImpl implements OrderItemService {
    private OrderItemDAO orderItemDAO;
    @Override
    public void addOrderItem(OrderItem orderItem) {
        orderItemDAO.addOrderItem(orderItem);
    }
}

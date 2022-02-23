package com.atguigu.book.dao.impl;

import com.atguigu.book.dao.OrderItemDAO;
import com.atguigu.book.pojo.OrderItem;
import com.atguigu.myssm.basedao.BaseDAO;

/**
 * @author Neo
 * @version 1.0
 */
public class OrderItemDAOImpl extends BaseDAO<OrderItem> implements OrderItemDAO {
    @Override
    public void addOrderItem(OrderItem orderItem) {
        executeUpdate("insert into t_order_item values(0,?,?,?)",
                orderItem.getBook().getId(),
                orderItem.getBuyCount(),
                orderItem.getOrderBean().getId());
    }
}

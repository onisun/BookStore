package com.atguigu.book.dao.impl;

import com.atguigu.book.dao.OrderDAO;
import com.atguigu.book.pojo.User;
import com.atguigu.myssm.basedao.BaseDAO;
import com.atguigu.book.pojo.OrderBean;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Neo
 * @version 1.0
 */
public class OrderDAOImpl extends BaseDAO<OrderBean> implements OrderDAO {
    @Override
    public void addOrderBean(OrderBean orderBean) {
        int orderBeanId = executeUpdate("insert into t_order values(0,?,?,?,?,?)",
                orderBean.getOrderNo(),
                orderBean.getOrderDate(),
                orderBean.getOrderUser().getId(),
                orderBean.getOrderMoney(),
                orderBean.getOrderStatus());
        orderBean.setId(orderBeanId);
    }

    @Override
    public List<OrderBean> getOrderList(User user) {
        return executeQuery("select * from t_order where orderUser = ?",user.getId());
    }

    @Override
    public Integer getOrderTotalBookCount(OrderBean orderBean) {
        String sql = "SELECT SUM(t3.buyCount) AS totalBookCount,t3.orderBean FROM " +
                "(" +
                "SELECT t1.id, t2.buyCount,t2.orderBean FROM t_order t1 " +
                "INNER JOIN t_order_item t2 " +
                "ON t1.id = t2.orderBean WHERE orderUser = ? " +
                ")t3 WHERE t3.orderBean = ? GROUP BY t3.orderBean";
        return ((BigDecimal)executeComplexQuery(sql,orderBean.getOrderUser().getId(),orderBean.getId())[0]).intValue();
    }
}

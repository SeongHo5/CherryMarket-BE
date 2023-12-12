package com.cherrydev.cherrymarketbe.order.repository;

import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

   void save(Order order);

   List<Order> findAllOrders();

   List<Order> findOrdersByAccountId(Long accountId);

   void updateOrderStatus(Order order);

}

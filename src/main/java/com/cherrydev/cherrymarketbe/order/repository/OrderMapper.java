package com.cherrydev.cherrymarketbe.order.repository;

import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import jakarta.validation.constraints.Email;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

   void save(Order order);

   List<Order> findAllOrders();

   List<Order> findAllOrdersByStatus(String orderStatus);

   List<Order> findOrdersSummaryByAccountId(Long accountId);
   void updateStatus(Order order);

   int countOrderByOrderCode(String orderCode);

   OrderStatus findOrderStatusByOrderCode(String orderCode);

   @Email String findAccountEmailByOrderCode(String orderCode);

   Order findOrderByOrderCode(String orderCode);

   Integer findAmountByOrderCode(String orderCode);


}

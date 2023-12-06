package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.OrderRequestChangeDto;
import com.cherrydev.cherrymarketbe.order.dto.OrderCreateDto;
import com.cherrydev.cherrymarketbe.order.dto.OrderResponseDto;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;


public interface OrderService {

    Map<OrderStatus, List<OrderResponseDto>> findAllOrders();
    List<OrderResponseDto> findOrdersByAccountId(AccountDetails accountDetails);
    void createOrder(AccountDetails accountDetails);
    void updateOrderStatus(OrderRequestChangeDto requestChangeDto);

}

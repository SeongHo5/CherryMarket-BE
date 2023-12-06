package com.cherrydev.cherrymarketbe.order.service;



import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.order.dto.OrderRequestChangeDto;
import com.cherrydev.cherrymarketbe.order.dto.OrderCreateDto;
import com.cherrydev.cherrymarketbe.order.dto.OrderResponseDto;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.order.repository.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService{

    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public Map<OrderStatus, List<OrderResponseDto>> findAllOrders() {
        List<Order> orders = orderMapper.findAllOrders();

        return orders.stream()
                .map(OrderResponseDto::getOrdersList)
                .collect(Collectors.groupingBy(OrderResponseDto::orderStatus));
    }

    @Transactional
    @Override
    public List<OrderResponseDto> findOrdersByAccountId(AccountDetails accountDetails) {
        List<Order> orders = orderMapper.findOrdersByAccountId(getAccountId(accountDetails));

        return orders.stream()
                .filter(order -> !order.isDeleted())
                .map(OrderResponseDto::getOrdersList)
                .toList();
    }

    @Transactional
    @Override
    public void createOrder(AccountDetails accountDetails) {
        Order order  = new OrderCreateDto().createOrder(getAccount(accountDetails));
        orderMapper.save(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(OrderRequestChangeDto requestChangeDto) {
        Order order = requestChangeDto.changeOrderStatus();
        orderMapper.updateOrderStatus(order);
    }

    public Long getAccountId(AccountDetails accountDetails){
        return accountDetails.getAccount().getAccountId();
    }

    public Account getAccount(AccountDetails accountDetails){
        return accountDetails.getAccount();
    }


}

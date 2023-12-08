package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.order.dto.*;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.repository.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.NOT_FOUND_ACCOUNT;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor

public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public ResponseEntity<AllUserOrderList> findAllUserOrders() {
        List<Order> orders = orderMapper.findAllOrders();
        return ResponseEntity
                .ok()
                .body(
                        new AllUserOrderList(orders)
                );
    }

    @Transactional
    @Override
    public ResponseEntity<UserOrderList> findOrdersByAccountId(AccountDetails accountDetails) {
        validateAccount(accountDetails);
        List<Order> orders =
                orderMapper.findOrdersByAccountId(accountDetails.getAccount().getAccountId());
        return ResponseEntity
                .ok()
                .body(
                        UserOrderListFactory.find(orders)
                );
    }

    @Transactional
    @Override
    public void createOrder(AccountDetails accountDetails) {
        validateAccount(accountDetails);
        Order order  = new CreateOrder().create(accountDetails.getAccount());
        orderMapper.save(order);
    }

    @Transactional
    @Override
    public void updateOrderStatus(ChangeOrderStatus requestChangeDto) {
        Order order = requestChangeDto.changeOrderStatus();
        orderMapper.updateOrderStatus(order);
    }

    private void validateAccount(AccountDetails accountDetails) {
        if (accountDetails == null
                || accountDetails.getAccount() == null
                || accountDetails.getAccount().getAccountId() == null
        ) {
            throw new NotFoundException(NOT_FOUND_ACCOUNT);
        }
    }

}

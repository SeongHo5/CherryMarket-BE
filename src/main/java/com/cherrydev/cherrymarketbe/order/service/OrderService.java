package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.AllUserOrderList;
import com.cherrydev.cherrymarketbe.order.dto.ChangeOrderStatus;
import com.cherrydev.cherrymarketbe.order.dto.UserOrderList;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<AllUserOrderList> findAllOrders();
    ResponseEntity<UserOrderList> findOrdersByAccountId(AccountDetails accountDetails);
    void createOrder(AccountDetails accountDetails);
    void updateOrderStatus(ChangeOrderStatus requestChangeDto);

}

package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderDetailsInfo;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderListGroupByStatusResponse;
import com.cherrydev.cherrymarketbe.order.dto.requests.OrderStatusRequest;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderSummaryList;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentConfirmForm;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<OrderListGroupByStatusResponse> findAllUserOrders();
    ResponseEntity<OrderListGroupByStatusResponse> findAllUserOrdersByStatus(String orderStatus);
    ResponseEntity<OrderSummaryList> findOrdersByAccountId(AccountDetails accountDetails);
    Payment confirmPaymentInfo(PaymentConfirmForm form);
    void createOrder(AccountDetails accountDetails, OrderReceiptResponse requestDto);
    void updateOrderStatus(OrderStatusRequest requestDto);
    void cancelOrder(String orderCode, PaymentCancelForm form);
    ResponseEntity<OrderDetailsInfo> findOrderDetails(AccountDetails accountDetails, String orderCode);

}

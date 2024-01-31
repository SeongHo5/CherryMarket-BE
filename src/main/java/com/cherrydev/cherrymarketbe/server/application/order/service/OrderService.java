package com.cherrydev.cherrymarketbe.server.application.order.service;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderDetailsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderListGroupByStatusResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.requests.OrderStatusRequest;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderSummaryList;
import com.cherrydev.cherrymarketbe.server.domain.payment.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.dto.PaymentApproveForm;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;
import org.springframework.http.ResponseEntity;

public interface OrderService {

    ResponseEntity<OrderListGroupByStatusResponse> findAllUserOrders();
    ResponseEntity<OrderListGroupByStatusResponse> findAllUserOrdersByStatus(String orderStatus);
    ResponseEntity<OrderSummaryList> findOrdersByAccountId(AccountDetails accountDetails);
    Payment confirmPaymentInfo(PaymentApproveForm form);
    void createOrder(AccountDetails accountDetails, OrderReceiptResponse requestDto);
    void updateOrderStatus(OrderStatusRequest requestDto);
    void cancelOrder(String orderCode, PaymentCancelForm form);
    ResponseEntity<OrderDetailsInfo> findOrderDetails(AccountDetails accountDetails, String orderCode);

}

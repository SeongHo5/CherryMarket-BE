package com.cherrydev.cherrymarketbe.server.application.order.service;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.server.application.payments.service.TossPaymentsService;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.cart.entity.Cart;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsDetailInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.request.RequestCreateOrder;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderDetailsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderInfoResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.OrderDetail;
import com.cherrydev.cherrymarketbe.server.domain.order.entity.Orders;
import com.cherrydev.cherrymarketbe.server.domain.payment.entity.PaymentDetail;
import com.cherrydev.cherrymarketbe.server.domain.payment.toss.model.TossPayment;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.order.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.cherrydev.cherrymarketbe.server.application.aop.exception.ExceptionStatus.NOT_FOUND_ORDER;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final TossPaymentsService tossPaymentsService;
    private final CartService cartService;

    @Transactional(readOnly = true)
    public Page<OrderInfoResponse> fetchAllMyOrders(
            final AccountDetails accountDetails,
            Pageable pageable
    ) {
        List<OrderInfoResponse> orders = ordersRepository.findAllByAccount(accountDetails.getAccount())
                .stream()
                .map(OrderInfoResponse::of)
                .toList();
        return new PageImpl<>(orders, pageable, orders.size());
    }

    @Transactional(readOnly = true)
    public OrderDetailsInfo fetchOrderDetails(
            final String orderCode
    ) {
        Orders orders = fetchOrdersEntity(orderCode);

        return handleFetchOrderDetailsInternal(orders);
    }

    private OrderDetailsInfo handleFetchOrderDetailsInternal(final Orders orders) {
        List<OrderDetail> orderDetail = orders.getOrderDetails();
        PaymentDetail paymentDetail = orders.getPaymentDetail();
        DeliveryDetail deliveryDetail = orders.getDeliveryDetail();

        List<GoodsDetailInfo> goodsDetail = orderDetail
                .stream()
                .map(OrderDetail::getGoods)
                .map(GoodsDetailInfo::of)
                .toList();

        return OrderDetailsInfo.of(orders.getCode().toString(), paymentDetail, deliveryDetail, goodsDetail);
    }

    @Transactional
    public void createOrder(
            AccountDetails accountDetails,
            RequestCreateOrder request
    ) {
        Orders orders = Orders.of(accountDetails.getAccount(), request.orderName());
        List<OrderDetail> cartItems = cartService.fetchCartItems(accountDetails)
                .stream()
                .map(cart -> OrderDetail.of(orders, cart))
                .toList();
        DeliveryDetail.of(orders, request);
        ordersRepository.save(orders);
    }

    @Transactional
    public void processOrder(
            AccountDetails accountDetails,
            final String tossPaymentKey,
            final String orderCode
    ) {
        Orders orders = fetchOrdersEntity(orderCode);
        TossPayment tossPayment = tossPaymentsService.findPaymentByPaymentKey(tossPaymentKey);
        PaymentDetail.of(orders, tossPayment);
    }


    private Orders fetchOrdersEntity(String orderCode) {
        return ordersRepository.findByCode(UUID.fromString(orderCode))
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_ORDER));
    }

}

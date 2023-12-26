package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.service.AccountService;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;
import com.cherrydev.cherrymarketbe.customer.service.RewardService;
import com.cherrydev.cherrymarketbe.goods.dto.ToCartResponseDto;
import com.cherrydev.cherrymarketbe.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.order.domain.*;
import com.cherrydev.cherrymarketbe.order.dto.responses.*;
import com.cherrydev.cherrymarketbe.order.dto.requests.OrderStatusRequest;
import com.cherrydev.cherrymarketbe.order.dto.requests.OrderDetailRequest;
import com.cherrydev.cherrymarketbe.order.entity.Order;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import com.cherrydev.cherrymarketbe.order.event.OrderCancelEvent;
import com.cherrydev.cherrymarketbe.order.event.OrderCreationEvent;
import com.cherrydev.cherrymarketbe.order.repository.OrderMapper;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentCancelForm;
import com.cherrydev.cherrymarketbe.payments.dto.PaymentConfirmForm;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
import com.cherrydev.cherrymarketbe.payments.service.TossPaymentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final TossPaymentsService tossPaymentsService;

    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final ProductService productService;
    private final GoodsService goodsService;
    private final RewardService rewardService;
    private final AccountService accountService;

    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Override
    public ResponseEntity<OrderListGroupByStatusResponse> findAllUserOrders() {
        List<Order> orders = orderMapper.findAllOrders();
        return ResponseEntity
                .ok()
                .body(
                        new OrderListGroupByStatusResponse(orders)
                );
    }

    public ResponseEntity<OrderListGroupByStatusResponse> findAllUserOrdersByStatus(String orderStatus) {
        List<Order> orders = orderMapper.findAllOrdersByStatus(orderStatus);
        return ResponseEntity
                .ok()
                .body(
                        new OrderListGroupByStatusResponse(orders)
                );
    }

    @Transactional
    @Override
    public ResponseEntity<OrderSummaryList> findOrdersByAccountId(AccountDetails accountDetails) {
        List<Order> orders =
                orderMapper.findOrdersSummaryByAccountId(accountDetails.getAccount().getAccountId());
        return ResponseEntity
                .ok()
                .body(
                        OrderSummaryListFactory.find(orders)
                );
    }

    @Override
    public ResponseEntity<OrderDetailsInfo> findOrderDetails(AccountDetails accountDetails, String orderCode) {
        ConsumerInfo consumerInfo = ConsumerInfo.getInfo(getOrderByOrderCode(orderCode), accountDetails);
        ShippingDetailsInfo shippingDetailsInfo = shippingService.findByOrderCode(orderCode);
        PaymentDetailsInfo paymentDetailsInfo = paymentService.getOrderWithPaymentDetail(orderCode);

        List<GoodsInfo> goodsInfos = productService.getGoodsInfo(orderCode);
        List<GoodsDetailsInfo> goodsDetailsInfoList = new ArrayList<>();

        for (GoodsInfo goodsInfo : goodsInfos) {
            ToCartResponseDto responseDto = goodsService.findToCart(goodsInfo.goodsId());

            GoodsDetailsInfo goodsDetailsInfo = GoodsDetailsInfo.getGoodsDetails(goodsInfo, responseDto);
            goodsDetailsInfoList.add(goodsDetailsInfo);
        }

        AmountInfo amountInfo = AmountInfo.getInfo(paymentDetailsInfo);
        OrderDetailsInfo orderDetailsInfo = new OrderDetailsInfo(consumerInfo, amountInfo, shippingDetailsInfo, goodsDetailsInfoList);

        return  ResponseEntity
                .ok()
                .body(
                        orderDetailsInfo
                );
    }

    @Override
    public Payment confirmPaymentInfo(PaymentConfirmForm form) {
        return tossPaymentsService.paymentConfirm(form);
    }

    @Transactional
    @Override
    public void createOrder(AccountDetails accountDetails, OrderReceiptResponse requestDto) {

        if (isOrderCodeExists(requestDto.orderCode())) {
            throw new DuplicatedException(CONFLICT_ORDER);
        }

        Order order = new OrderDetailRequest().create(
                accountDetails,
                requestDto,
                getOrderWithPayment(requestDto.orderCode())
        );

        orderMapper.save(order);

        OrderCreationEvent event =
                new OrderCreationEvent(
                        this,
                        accountDetails,
                        requestDto,
                        getOrderWithPayment(requestDto.orderCode()),
                        order.getOrderId());

        eventPublisher.publishEvent(event);
    }

    @Transactional
    @Override
    public void updateOrderStatus(OrderStatusRequest requestDto) {

        Order order = requestDto.changeOrderStatus();

        if(requestDto.orderStatus().equals("COMPLETED")){
            String accountEmail = orderMapper.findAccountEmailByOrderCode(order.getOrderCode());
            Integer orderAmount= orderMapper.findAmountByOrderCode(order.getOrderCode());

            AddRewardRequestDto rewardRequestDto = AddRewardRequestDto.builder()
                    .email(accountEmail)
                    .rewardGrantType("PURCHASE")
                    .amounts((int)(Math.round(orderAmount * 0.1)))
                    .earnedAt(String.valueOf(LocalDate.now()))
                    .expiredAt((LocalDate.now()).plusMonths(6).toString())
                    .build();
            rewardService.grantReward(rewardRequestDto);
        }

        orderMapper.updateStatus(order);
    }

    @Transactional
    @Override
    public void cancelOrder(String orderCode, PaymentCancelForm form) {

        if (orderCode == null) {
            throw new NotFoundException(NOT_FOUND_ORDERCODE);
        }
        OrderStatus orderStatus = getOrderStatusByOrderCode(orderCode);

        if (orderStatus == OrderStatus.CANCELED) {
            throw new ServiceFailedException(CONFLICT_CANCELED_ORDER);
        }

        String paymentKey = getOrderWithPayment(orderCode).getPaymentKey();

        if (paymentKey == null) {
            throw new NotFoundException(NOT_FOUND_PAYMENT);
        }

        Payment paymentResponse = tossPaymentsService.paymentCancel(paymentKey, form);
        String responseStatus = String.valueOf(paymentResponse.getStatus());

//            Payment paymentResponse = getOrderWithPayment(orderCode);
//            String responseStatus = String.valueOf(paymentResponse.getStatus());

        if (responseStatus.equals("CANCELED") || responseStatus.equals("PARTIAL_CANCELED")) {
            Order order = OrderStatusRequest.builder()
                    .orderCode(orderCode)
                    .orderStatus(responseStatus)
                    .build()
                    .changeOrderStatus();

            orderMapper.updateStatus(order);
        }

        Integer orderAmount= orderMapper.findAmountByOrderCode(orderCode);
        String accountEmail = orderMapper.findAccountEmailByOrderCode(orderCode);

        OrderCancelEvent event = new OrderCancelEvent(this, accountEmail, orderAmount, orderCode, paymentResponse);
        eventPublisher.publishEvent(event);
    }

    private Payment getOrderWithPayment(String orderCode) {
        Payment payment = tossPaymentsService.findPaymentByOrderId(orderCode);

        if (payment == null) {
            throw new ServiceFailedException(INVALID_INPUT_VALUE);
        }
        return payment;

    }

    private boolean isOrderCodeExists(String orderCode) {
        return orderMapper.countOrderByOrderCode(orderCode) > 0;
    }

    private OrderStatus getOrderStatusByOrderCode(String orderCode) {
        return orderMapper.findOrderStatusByOrderCode(orderCode);
    }

    private Order getOrderByOrderCode(String orderCode) {
        return orderMapper.findOrderByOrderCode(orderCode);
    }



}

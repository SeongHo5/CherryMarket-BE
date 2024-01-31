package com.cherrydev.cherrymarketbe.server.application.order.event.listner;

import com.cherrydev.cherrymarketbe.server.application.order.event.OrderCancelEvent;
import com.cherrydev.cherrymarketbe.server.application.order.event.OrderCreationEvent;
import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.customer.dto.request.RequestAddReward;
import com.cherrydev.cherrymarketbe.server.application.customer.service.RewardService;
import com.cherrydev.cherrymarketbe.server.application.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.enums.ShippingStatus;
import com.cherrydev.cherrymarketbe.server.application.order.service.ShippingService;
import com.cherrydev.cherrymarketbe.server.application.order.service.ProductService;
import com.cherrydev.cherrymarketbe.server.application.order.service.PaymentService;
import com.cherrydev.cherrymarketbe.server.domain.payment.model.payment.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListner {

    private final ProductService productService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final RewardService rewardService;
    private final GoodsService goodsService;

    @EventListener
    public void handleCreateOrderDetailEvent(OrderCreationEvent event) {
        AccountDetails accountDetails = event.getAccountDetails();
        OrderReceiptResponse requestDto = event.getRequestDto();
        Payment payment = event.getPayment();
        Long orderId = event.getOrderId();

        RequestAddReward rewardRequestDto = RequestAddReward.builder()
                .email(accountDetails.getAccount().getEmail())
                .rewardGrantType("USE")
                .amounts((int)(Math.round(payment.getTotalAmount() * 0.1)))
                .earnedAt(String.valueOf(LocalDate.now()))
                .expiredAt(String.valueOf(LocalDate.now()))
                .build();

        productService.createOrderDetail(accountDetails, requestDto, orderId);
        paymentService.createPaymentDetail(accountDetails, requestDto, payment, orderId);
        shippingService.createDeliveryDetail(accountDetails, orderId, requestDto.orderCode());
        rewardService.grantReward(rewardRequestDto);

        requestDto.goodsInfo()
                .forEach(goodsInfo -> {
                    Long goodsId = goodsInfo.goodsId();
                    Integer quantity = goodsInfo.quantity();
                    goodsService.updateGoodsInventory(goodsId, quantity);
                });
    }

    @EventListener
    public void handleCancelOrderDetailEvent(OrderCancelEvent event) {
        String accountEmail = event.getAccountEmail();
        Payment payment = event.getPayment();
        String orderCode = event.getOrderCode();
        Integer rewardAmount = event.getRewardAmount();

        paymentService.cancelPaymentDetail(orderCode, payment);
        shippingService.updateDeliveryDetail(orderCode, ShippingStatus.CANCELED.toString());
        List<GoodsInfo> findGoodsInfo = productService.getGoodsInfo(orderCode);

        findGoodsInfo
                .forEach(goodsInfo -> {
                    Long goodsId = goodsInfo.goodsId();
                    Integer quantity = goodsInfo.quantity();
                    goodsService.updateGoodsInventory(goodsId, -quantity);
                });



        RequestAddReward rewardRequestDto = RequestAddReward.builder()
                .email(accountEmail)
                .rewardGrantType("PURCHASE")
                .amounts((int)(Math.round(rewardAmount * -0.1)))
                .earnedAt(String.valueOf(LocalDate.now()))
                .expiredAt(String.valueOf(LocalDate.now()))
                .build();

        rewardService.grantReward(rewardRequestDto);
    }

}

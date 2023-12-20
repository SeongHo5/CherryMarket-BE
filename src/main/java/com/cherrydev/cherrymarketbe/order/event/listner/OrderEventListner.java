package com.cherrydev.cherrymarketbe.order.event.listner;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;
import com.cherrydev.cherrymarketbe.customer.service.RewardService;
import com.cherrydev.cherrymarketbe.goods.service.GoodsService;
import com.cherrydev.cherrymarketbe.order.domain.GoodsInfo;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.order.enums.ShippingStatus;
import com.cherrydev.cherrymarketbe.order.event.OrderCancelEvent;
import com.cherrydev.cherrymarketbe.order.event.OrderCreationEvent;
import com.cherrydev.cherrymarketbe.order.service.ShippingService;
import com.cherrydev.cherrymarketbe.order.service.ProductService;
import com.cherrydev.cherrymarketbe.order.service.PaymentService;
import com.cherrydev.cherrymarketbe.payments.model.payment.Payment;
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

        AddRewardRequestDto rewardRequestDto = AddRewardRequestDto.builder()
                .email(accountDetails.getAccount().getEmail())
                .rewardGrantType("USE")
                .amounts(requestDto.amountInfo().rewordAmount())
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



        AddRewardRequestDto rewardRequestDto = AddRewardRequestDto.builder()
                .email(accountEmail)
                .rewardGrantType("PURCHASE")
                .amounts((int)(Math.round(rewardAmount * -0.1)))
                .earnedAt(String.valueOf(LocalDate.now()))
                .expiredAt(String.valueOf(LocalDate.now()))
                .build();

        rewardService.grantReward(rewardRequestDto);
    }

}

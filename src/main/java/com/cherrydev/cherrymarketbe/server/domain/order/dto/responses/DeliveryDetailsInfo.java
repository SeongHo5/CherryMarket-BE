package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import com.cherrydev.cherrymarketbe.server.domain.order.entity.DeliveryDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
public record DeliveryDetailsInfo(

        String shippingStatus,
        String recipient,
        String recipientContact,
        String zipCode,
        String address,
        String addressDetail,
        String place,
        String request
) {
    public static DeliveryDetailsInfo of(DeliveryDetail deliveryDetail) {
        return DeliveryDetailsInfo.builder()
                .shippingStatus(deliveryDetail.getDeliveryStatus())
                .recipient(deliveryDetail.getRecipient())
                .recipientContact(deliveryDetail.getRecipientContact())
                .zipCode(deliveryDetail.getZipCode())
                .address(deliveryDetail.getRoadNameAddress())
                .addressDetail(deliveryDetail.getAddressDetail())
                .place(deliveryDetail.getDeliveryPlace())
                .request(deliveryDetail.getDeliveryRequest())
                .build();
    }
}

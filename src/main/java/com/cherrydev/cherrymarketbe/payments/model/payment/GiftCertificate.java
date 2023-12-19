package com.cherrydev.cherrymarketbe.payments.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GiftCertificate {

    @JsonProperty("approveNo")
    private String approveNo;

    @JsonProperty("settlementStatus")
    private SettlementStatus settlementStatus;

}
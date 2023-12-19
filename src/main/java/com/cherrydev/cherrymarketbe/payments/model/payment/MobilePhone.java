package com.cherrydev.cherrymarketbe.payments.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobilePhone {

    @JsonProperty("customerMobilePhone")
    private String customerMobilePhone;

    @JsonProperty("settlementStatus")
    private SettlementStatus settlementStatus;

    @JsonProperty("receiptUrl")
    private String receiptUrl;

    // Ensure that the SettlementStatus class or enum is also defined in your Java codebase
}
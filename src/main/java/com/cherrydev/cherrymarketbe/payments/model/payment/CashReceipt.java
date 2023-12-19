package com.cherrydev.cherrymarketbe.payments.model.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CashReceipt {
    @JsonProperty("receiptKey")
    private String receiptKey;

    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("taxFreeAmount")
    private Long taxFreeAmount;

    @JsonProperty("issueNumber")
    private String issueNumber;

    @JsonProperty("receiptUrl")
    private String receiptUrl;
}
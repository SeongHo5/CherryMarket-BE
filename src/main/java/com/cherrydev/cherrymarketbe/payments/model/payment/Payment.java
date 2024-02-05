package com.cherrydev.cherrymarketbe.payments.model.payment;

import com.cherrydev.cherrymarketbe.payments.model.Failure;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class Payment {

    @JsonProperty("version")
    private String version;

    @JsonProperty("paymentKey")
    private String paymentKey;

    private Type type;

    @JsonProperty("orderCode")
    private String orderId;

    @JsonProperty("orderName")
    private String orderName;

    @JsonProperty("mId")
    private String mId;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("method")
    private String method;

    @JsonProperty("totalAmount")
    private long totalAmount;

    @JsonProperty("balanceAmount")
    private long balanceAmount;

    private Status status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonProperty("requestedAt")
    private OffsetDateTime requestedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @JsonProperty("approvedAt")
    private OffsetDateTime approvedAt;

    @JsonProperty("useEscrow")
    private boolean useEscrow;

    @JsonProperty("lastTransactionKey")
    private String lastTransactionKey;

    @JsonProperty("suppliedAmount")
    private long suppliedAmount;

    @JsonProperty("vat")
    private long vat;

    @JsonProperty("cultureExpense")
    private boolean cultureExpense;

    @JsonProperty("taxFreeAmount")
    private long taxFreeAmount;

    @JsonProperty("taxExemptionAmount")
    private long taxExemptionAmount;

    private List<Cancel> cancels;

    @JsonProperty("isPartialCancelable")
    private boolean isPartialCancelable;

    private Card card;

    private VirtualAccount virtualAccount;

    @JsonProperty("secret")
    private String secret;

    private MobilePhone mobilePhone;

    private GiftCertificate giftCertificate;

    private Transfer transfer;

    private Receipt receipt;

    private Checkout checkout;

    private EasyPay easyPay;

    @JsonProperty("country")
    private String country;

    private Failure failure;

    private CashReceipt cashReceipt;

    private Discount discount;

    @Builder
    public Payment(String version,
                   String paymentKey,
                   Type type,
                   String orderId,
                   String orderName,
                   String mId,
                   String currency,
                   String method,
                   long totalAmount,
                   long balanceAmount,
                   Status status,
                   OffsetDateTime requestedAt,
                   OffsetDateTime approvedAt,
                   boolean useEscrow,
                   String lastTransactionKey,
                   long suppliedAmount,
                   long vat,
                   boolean cultureExpense,
                   long taxFreeAmount,
                   long taxExemptionAmount,
                   List<Cancel> cancels,
                   boolean isPartialCancelable,
                   Card card,
                   VirtualAccount virtualAccount,
                   String secret,
                   MobilePhone mobilePhone,
                   GiftCertificate giftCertificate,
                   Transfer transfer,
                   Receipt receipt,
                   Checkout checkout,
                   EasyPay easyPay,
                   String country,
                   Failure failure,
                   CashReceipt cashReceipt,
                   Discount discount
    ) {
        this.version = version;
        this.paymentKey = paymentKey;
        this.type = type;
        this.orderId = orderId;
        this.orderName = orderName;
        this.mId = mId;
        this.currency = currency;
        this.method = method;
        this.totalAmount = totalAmount;
        this.balanceAmount = balanceAmount;
        this.status = status;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.useEscrow = useEscrow;
        this.lastTransactionKey = lastTransactionKey;
        this.suppliedAmount = suppliedAmount;
        this.vat = vat;
        this.cultureExpense = cultureExpense;
        this.taxFreeAmount = taxFreeAmount;
        this.taxExemptionAmount = taxExemptionAmount;
        this.cancels = cancels;
        this.isPartialCancelable = isPartialCancelable;
        this.card = card;
        this.virtualAccount = virtualAccount;
        this.secret = secret;
        this.mobilePhone = mobilePhone;
        this.giftCertificate = giftCertificate;
        this.transfer = transfer;
        this.receipt = receipt;
        this.checkout = checkout;
        this.easyPay = easyPay;
        this.country = country;
        this.failure = failure;
        this.cashReceipt = cashReceipt;
        this.discount = discount;
    }
}

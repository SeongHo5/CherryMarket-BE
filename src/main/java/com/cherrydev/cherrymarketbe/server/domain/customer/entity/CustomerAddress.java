package com.cherrydev.cherrymarketbe.server.domain.customer.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerAddress {

    private Long addressId;

    @NotNull
    private Long accountId;

    @NotNull
    private Boolean isDefault;

    @Size(max = 20)
    private String name;

    @Size(max = 10)
    @NotNull
    private String zipCode;

    @Size(max = 100)
    @NotNull
    private String address;

    @Size(max = 100)
    @NotNull
    private String addressDetail;

    private Timestamp createdAt;

    private Timestamp updatedAt;

}

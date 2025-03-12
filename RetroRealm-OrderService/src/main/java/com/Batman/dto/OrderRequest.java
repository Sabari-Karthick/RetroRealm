package com.Batman.dto;

import java.util.Set;

import com.Batman.enums.OrderType;
import com.Batman.enums.PaymentType;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {

    @NotNull(message = "User Id cannot be Empty")
    private Integer userId;

    @NotNull(message = "Game Ids are mandatory")
    @NotEmpty(message = "Game Ids cannot be Empty")
    private Set<Integer> gameIds;
    @NotNull(message = "Price cannot be Empty")
    private Double totalPrice;
    @NotNull(message = "Provide a Payment Type")
    private PaymentType paymentType;
    @NotNull(message = "Provide a Order Type")
    private OrderType orderType;

}

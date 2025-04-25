package com.example.delivery.domain.order.dto.Request;

import lombok.Getter;

@Getter
public class CartItemRequestDto {

    private Long menuId;

    private int quantity;
}

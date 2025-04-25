package com.example.delivery.domain.order.dto.Response.Cart;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemResponseDto {
    private Long id;
    private Long menuId;
    private String menuName;
    private int quantity;
}
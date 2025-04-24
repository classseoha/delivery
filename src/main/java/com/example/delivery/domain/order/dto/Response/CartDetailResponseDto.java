package com.example.delivery.domain.order.dto.Response;

import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;

import java.util.List;

public class CartDetailResponseDto {
    private Long cartId;
    private Long storeId;
    private String storeName;
    private List<CartItemDetailResponseDto> items;
    private Long totalAmount;

    public CartDetailResponseDto(Cart cart, List<CartItem> cartItems) {
        this.cartId = cart.getId();
        this.storeId = cart.getStore().getId();
        this.storeName = cart.getStore().getStoreName();
        this.items = cartItems.stream()
                .map(CartItemDetailResponseDto::new)
                .toList();
        this.totalAmount = items.stream()
                .mapToLong(CartItemDetailResponseDto::getTotalPrice)
                .sum();
    }
}
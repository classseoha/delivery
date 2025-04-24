package com.example.delivery.domain.order.dto.Response;

import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class CartDetailResponseDto {
    private final Long cartId;
    private final Long storeId;
    private final String storeName;
    private final List<CartItemDetailResponseDto> items;
    private final Long totalAmount;

    public static CartDetailResponseDto from(Cart cart, List<CartItem> cartItems) {
        List<CartItemDetailResponseDto> items = cartItems.stream()
                .map(CartItemDetailResponseDto::new)
                .toList();

        Long totalAmount = items.stream()
                .mapToLong(CartItemDetailResponseDto::getTotalPrice)
                .sum();

        return new CartDetailResponseDto(
                cart.getId(),
                cart.getStore().getId(),
                cart.getStore().getStoreName(),
                items,
                totalAmount
        );
    }
}

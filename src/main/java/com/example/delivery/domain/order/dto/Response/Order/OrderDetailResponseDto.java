package com.example.delivery.domain.order.dto.Response.Order;

import com.example.delivery.domain.order.dto.Response.Cart.CartItemDetailResponseDto;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import com.example.delivery.domain.order.entity.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderDetailResponseDto {
    private final Long orderId;
    private final Long cartId;
    private final Long storeId;
    private final String storeName;
    private final List<CartItemDetailResponseDto> items;
    private final Long totalAmount;
    private final String orderStatus;

    public static OrderDetailResponseDto from(Order order, List<CartItem> cartItems) {
        Cart cart = order.getCart();

        List<CartItemDetailResponseDto> items = cartItems.stream()
                .map(CartItemDetailResponseDto::new)
                .toList();

        long totalAmount = items.stream()
                .mapToLong(CartItemDetailResponseDto::getTotalPrice)
                .sum();

        return new OrderDetailResponseDto(
                order.getId(),
                cart.getId(),
                cart.getStore().getId(),
                cart.getStore().getStoreName(),
                items,
                totalAmount,
                order.getOrderStatus().name()
        );
    }
}


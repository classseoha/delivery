package com.example.delivery.domain.order.dto.Response.Order;

import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import com.example.delivery.domain.order.entity.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderResponseDto {
    private final Long orderId;
    private final Long cartId;
    private final Long storeId;
    private final String storeName;
    private final String menuSummary;
    private final Long totalAmount;
    private final String orderStatus;

    public static OrderResponseDto from(Order order, List<CartItem> cartItems) {
        Cart cart = order.getCart();

        String menuSummary = createMenuSummary(cartItems);
        long totalAmount = cartItems.stream()
                .mapToLong(item -> item.getMenu().getPrice() * item.getQuantity())
                .sum();

        return new OrderResponseDto(
                order.getId(),
                cart.getId(),
                cart.getStore().getId(),
                cart.getStore().getStoreName(),
                menuSummary,
                totalAmount,
                order.getOrderStatus().name()
        );
    }

    private static String createMenuSummary(List<CartItem> cartItems) {
        String firstMenuName = cartItems.get(0).getMenu().getMenuName();
        int extra = cartItems.size() - 1;
        return extra > 0 ? firstMenuName + " 외 " + extra + "개" : firstMenuName;
    }
}

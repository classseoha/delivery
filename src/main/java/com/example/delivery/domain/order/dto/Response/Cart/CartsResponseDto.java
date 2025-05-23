package com.example.delivery.domain.order.dto.Response.Cart;

import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CartsResponseDto {
    private final Long cartId;
    private final Long storeId;
    private final String storeName;
    private final String menuSummary; // 예: "불고기버거 외 2개"
    private final Long totalAmount;

    // 장바구니에 담긴 메뉴 총 가격
    public CartsResponseDto(Cart cart, List<CartItem> cartItems) {
        this.cartId = cart.getId();
        this.storeId = cart.getStore().getId();
        this.storeName = cart.getStore().getStoreName();
        this.totalAmount = cartItems.stream()
                .mapToLong(item -> item.getMenu().getPrice() * item.getQuantity())
                .sum();

        this.menuSummary = createMenuSummary(cartItems);
    }

    // 장바구니 메뉴 요약
    private String createMenuSummary(List<CartItem> cartItems) {
        String firstMenuName = cartItems.get(0).getMenu().getMenuName();

        int extraCount = cartItems.size() - 1;

        return extraCount > 0 ? firstMenuName + " 외 " + extraCount + "개" : firstMenuName;
    }
}
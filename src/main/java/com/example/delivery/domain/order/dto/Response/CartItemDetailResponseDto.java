package com.example.delivery.domain.order.dto.Response;

import com.example.delivery.domain.order.entity.CartItem;
import lombok.Getter;

@Getter
public class CartItemDetailResponseDto {
    private Long menuId;
    private String menuName;
    private String intro;
    private Long price;
    private int quantity;
    private Long totalPrice;

    public CartItemDetailResponseDto(CartItem item) {
        this.menuId = item.getMenu().getId();
        this.menuName = item.getMenu().getMenuName();
        this.intro = item.getMenu().getIntro();
        this.price = item.getMenu().getPrice();
        this.quantity = item.getQuantity();
        this.totalPrice = price * quantity;
    }
}
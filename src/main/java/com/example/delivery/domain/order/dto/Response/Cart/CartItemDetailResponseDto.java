package com.example.delivery.domain.order.dto.Response.Cart;

import com.example.delivery.domain.order.entity.CartItem;
import lombok.Value;

@Value
public class CartItemDetailResponseDto {
    Long menuId;
    String menuName;
    String intro;
    Long price;
    int quantity;
    Long totalPrice;

    public CartItemDetailResponseDto(CartItem item) {
        this.menuId = item.getMenu().getId();
        this.menuName = item.getMenu().getMenuName();
        this.intro = item.getMenu().getIntro();
        this.price = item.getMenu().getPrice();
        this.quantity = item.getQuantity();
        this.totalPrice = price * quantity;
    }
}
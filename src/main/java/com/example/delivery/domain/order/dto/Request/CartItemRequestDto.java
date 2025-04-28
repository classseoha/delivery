package com.example.delivery.domain.order.dto.Request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class CartItemRequestDto {

    private Long menuId;

    private int quantity;


    /*@JsonCreator
    public CartItemRequestDto(
            @JsonProperty("menuId")Long menuId,
            @JsonProperty("quantity")int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }*/
}
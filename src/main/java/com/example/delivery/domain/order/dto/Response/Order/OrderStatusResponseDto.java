package com.example.delivery.domain.order.dto.Response.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderStatusResponseDto {
    private final Long orderId;
    private final String orderStatus;

    public static OrderStatusResponseDto from(Long orderId, String orderStatus) {
        return new OrderStatusResponseDto(orderId, orderStatus);
    }
}


package com.example.delivery.domain.store.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CreateResponseDto {
    private final Long id;
    private final String storeName;
    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final Long minAmount;
    private final String storeStatus;


    public CreateResponseDto(Long id, String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount, String storeStatus) {
        this.id = id;
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minAmount = minAmount;
        this.storeStatus = storeStatus;
    }
}

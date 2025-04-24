package com.example.delivery.domain.store.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UpdateRequestDto {

    private final String storeName;
    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final Long minAmount;


    public UpdateRequestDto(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount, String storeStatus) {
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minAmount = minAmount;
    }
}

package com.example.delivery.domain.store.dto;

import com.example.delivery.domain.store.entity.StoreStatus;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UpdateResponseDto {
    private final String storeName;
    private final LocalTime openingTime;
    private final LocalTime closingTime;
    private final Long minAmount;
    private final StoreStatus storeStatus;

    public UpdateResponseDto(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount, StoreStatus storeStatus) {
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minAmount = minAmount;
        this.storeStatus = storeStatus;
    }
}

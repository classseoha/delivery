package com.example.delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class UpdateRequestDto {

    @NotBlank
    private final String storeName;

    @NotNull
    private final LocalTime openingTime;

    @NotNull
    private final LocalTime closingTime;

    @NotBlank
    private final Long minAmount;


    public UpdateRequestDto(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount, String storeStatus) {
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minAmount = minAmount;
    }
}

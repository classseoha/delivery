package com.example.delivery.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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


    @JsonCreator
    public UpdateRequestDto(
            @JsonProperty("storeName")String storeName,
            @JsonProperty("openingTime")LocalTime openingTime,
            @JsonProperty("closingTime")LocalTime closingTime,
            @JsonProperty("minAmount")Long minAmount) {
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minAmount = minAmount;
    }
}

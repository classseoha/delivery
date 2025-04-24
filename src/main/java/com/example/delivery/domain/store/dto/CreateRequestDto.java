package com.example.delivery.domain.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class CreateRequestDto {
 /*
 * @NotBlank는 String 타입에만 적용되므로, LocalTime, Long 같은 타입에는 다른 유효성 검사 애너테이션을 사용하는 것이 더 적합하다고 합니다.
  */
    @NotBlank
    private final String storeName;

    @NotNull
    private final LocalTime openingTime;

    @NotNull
    private final LocalTime closingTime;

    @NotNull
    @Positive // 숫자 필드가 양수인지를 검사합니다.
    private final Long minAmount;

    @NotBlank
    private final String storeStatus;

    public CreateRequestDto(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount, String storeStatus) {
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minAmount = minAmount;
        this.storeStatus = storeStatus;
    }
}

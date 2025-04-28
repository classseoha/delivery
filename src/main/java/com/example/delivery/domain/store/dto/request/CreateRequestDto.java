package com.example.delivery.domain.store.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
public class CreateRequestDto {
 /*
 * @NotBlank는 String 타입에만 적용되므로, LocalTime, Long 같은 타입에는 다른 유효성 검사 애너테이션을 사용하는 것이 더 적합하다고 합니다.
  */
    @NotBlank
    private final String storeName;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime openingTime;

    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private final LocalTime closingTime;

    @NotNull
    @Positive // 숫자 필드가 양수인지를 검사합니다.
    private final Long minAmount;

    @JsonCreator
    public CreateRequestDto(
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

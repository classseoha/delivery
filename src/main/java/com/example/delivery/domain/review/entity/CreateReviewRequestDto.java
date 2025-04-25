package com.example.delivery.domain.review.entity;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CreateReviewRequestDto {

    private final Long orderId;

    private final Long userId;

    private final Long storeId;

    @Min(value = 1,message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5,message = "평점은 5점 이하여야 합니다.")
    private final Integer rating;

    private final String content;

    public CreateReviewRequestDto(Long orderId, Long userId, Long storeId, Integer rating, String content) {
        this.orderId = orderId;
        this.userId = userId;
        this.storeId = storeId;
        this.rating = rating;
        this.content = content;
    }
}

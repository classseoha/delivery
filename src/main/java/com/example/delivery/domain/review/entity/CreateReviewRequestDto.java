package com.example.delivery.domain.review.entity;


import lombok.Getter;

@Getter
public class CreateReviewRequestDto {

    private final Long userId;

    private final Long storeId;

    private final Integer rating;

    private final String content;

    public CreateReviewRequestDto(Long userId, Long storeId, Integer rating, String content) {
        this.userId = userId;
        this.storeId = storeId;
        this.rating = rating;
        this.content = content;
    }
}

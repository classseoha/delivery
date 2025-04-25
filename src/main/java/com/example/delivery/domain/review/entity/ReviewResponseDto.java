package com.example.delivery.domain.review.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private final Long id;
    private final Integer rating;
    private final String content;
    private final LocalDateTime createdAt;

    public static ReviewResponseDto from(Review review) {
        return new ReviewResponseDto(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getCreatedAt()
        );
    }
}

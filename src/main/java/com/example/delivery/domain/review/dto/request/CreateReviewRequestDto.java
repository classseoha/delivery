package com.example.delivery.domain.review.dto.request;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CreateReviewRequestDto {

    private final Long storeId;

    @Min(value = 1,message = "평점은 1점 이상이어야 합니다.")
    @Max(value = 5,message = "평점은 5점 이하여야 합니다.")
    private final Integer rating;

    private final String content;

    @JsonCreator
    public CreateReviewRequestDto(
            @JsonProperty("storeId")Long storeId,
            @JsonProperty("rating")Integer rating,
            @JsonProperty("content")String content) {
        this.storeId = storeId;
        this.rating = rating;
        this.content = content;
    }
}

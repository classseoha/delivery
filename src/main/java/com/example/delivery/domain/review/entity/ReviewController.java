package com.example.delivery.domain.review.entity;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<Long> createReview(
            @PathVariable Long orderId,
            @RequestBody CreateReviewRequestDto requsetDto
    ){

        Long reviewId = reviewService.createReview(
                requsetDto.getUserId(),
                orderId,
                requsetDto.getStoreId(),
                requsetDto.getRating(),
                requsetDto.getContent()
        );
       return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }

}

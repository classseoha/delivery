package com.example.delivery.domain.review.entity;


import jakarta.validation.Valid;
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
            @RequestBody @Valid CreateReviewRequestDto requestDto
    ){

        Long reviewId = reviewService.createReview(
                requestDto.getUserId(),
                orderId,
                requestDto.getStoreId(),
                requestDto.getRating(),
                requestDto.getContent()
        );
       return ResponseEntity.status(HttpStatus.CREATED).body(reviewId);
    }

}

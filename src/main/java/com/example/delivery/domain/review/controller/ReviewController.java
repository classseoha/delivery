package com.example.delivery.domain.review.controller;



import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.review.dto.request.CreateReviewRequestDto;
import com.example.delivery.domain.review.dto.response.ReviewResponseDto;
import com.example.delivery.domain.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/reviews")
    public ResponseEntity<ApiResponseDto<Long>> createReview(
            @RequestParam Long orderId,
            @RequestBody @Valid CreateReviewRequestDto requestDto,
            HttpServletRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Long reviewId = reviewService.createReview(orderId,requestDto);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.CREATE_REVIEW,reviewId,request.getRequestURI()));
    }


    @GetMapping("/{storeId}/reviews")
    public ResponseEntity<ApiResponseDto<List<ReviewResponseDto>>> getReviews(
            @PathVariable Long storeId, // URL 경로에서 가게 ID 추출
            @RequestParam(defaultValue = "1") Integer minRating, // 쿼리 파라미터 - 최소 평점
            @RequestParam(defaultValue = "5") Integer maxRating,  // 쿼리 파라미터 - 최대 평점
            HttpServletRequest request
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        List<ReviewResponseDto> reviews = reviewService.getReviews(storeId, minRating, maxRating);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_REVIEW_SUCESS,reviews,request.getRequestURI()));
    }
}

package com.example.delivery.domain.review.entity;


import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponseDto<Long>> createReview(
            @PathVariable Long orderId,
            @RequestBody @Valid CreateReviewRequestDto requestDto,
            HttpServletRequest request
    ) {
        // URL에 입력된 orderId 값과 requestDto orderId 값 비교
        if(!orderId.equals(requestDto.getOrderId())){
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        Long reviewId = reviewService.createReview(requestDto);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.CREATE_REVIEW,reviewId,request.getRequestURI()));
    }


    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<ApiResponseDto<List<ReviewResponseDto>>> getReviews(
            @PathVariable Long storeId, // URL 경로에서 가게 ID 추출
            @RequestParam(defaultValue = "1") Integer minRating, // 쿼리 파라미터 - 최소 평점
            @RequestParam(defaultValue = "5") Integer maxRating,  // 쿼리 파라미터 - 최대 평점
            HttpServletRequest request
    ) {
        List<ReviewResponseDto> reviews = reviewService.getReviews(storeId, minRating, maxRating);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_REVIEW_SUCESS,reviews,request.getRequestURI()));
    }
}

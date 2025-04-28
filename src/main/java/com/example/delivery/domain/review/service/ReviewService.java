package com.example.delivery.domain.review.service;


import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.domain.review.dto.CreateReviewRequestDto;
import com.example.delivery.domain.review.dto.ReviewResponseDto;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public Long createReview(Long orderId, CreateReviewRequestDto requestDto) {

        // 1. 주문조회
        Order order = orderRepository.findByIdOrElseThrow(orderId);

        // 2. 가게조회
        Store store = storeRepository.findByIdOrElseThrow(requestDto.getStoreId());

        // 3. 배달 상태 확인
        if (!order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            throw new CustomException(ErrorCode.REVIEW_NOT_ALLOWED);
        }
        // 4. 리뷰 생성
        Review review = Review.create(order, order.getUser(), store, requestDto.getRating(), requestDto.getContent());
        // 5. 레포지토리에 저장
        reviewRepository.save(review);
        // 6. reviewId 반환
        return review.getId();
    }


    @Transactional
    public List<ReviewResponseDto> getReviews(Long storeId, Integer minRating, Integer maxRating) {
        return reviewRepository.findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(storeId, minRating, maxRating)
                .stream() // List<Review> → Stream<Review>
                .map(ReviewResponseDto::from) // 각 Review 객체를 ReviewResponseDto로 변환
                .toList(); // 다시 List로 수집
    }


}

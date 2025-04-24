package com.example.delivery.domain.review.entity;


import ch.qos.logback.core.spi.ErrorCodes;
import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderRepository;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.entity.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public Long createReview(Long userId, Long orderId, Long storeId, Integer rating, String content) {

        // 1. 주문조회
        Order order = orderRepository.findByIdOrElseThrow(orderId);

        // 2. 가게조회
        Store store = storeRepository.findByIdOrElseThrow(storeId);

        // 3. 리뷰 작성 권한 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);

        }
        // 4. 배달 상태 확인
        if (!order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            throw new CustomException(ErrorCode.REVIEW_NOT_ALLOWED);
        }
        // 5. 리뷰 생성
        Review review = Review.create(order, order.getUser(), store, rating, content);
        // 6. 레포지토리에 저장
        reviewRepository.save(review);
        // 7. reviewId 반환
        return review.getId();
    }

    public List<ReviewResponseDto> getReviews(Long storeId, Integer minRating, Integer maxRating) {
        return reviewRepository.findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(storeId, minRating, maxRating)
                .stream() // List<Review> → Stream<Review>
                .map(ReviewResponseDto::from) // 각 Review 객체를 ReviewResponseDto로 변환
                .toList(); // 다시 List로 수집
    }


}

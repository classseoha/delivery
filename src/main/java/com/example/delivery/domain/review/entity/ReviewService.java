package com.example.delivery.domain.review.entity;


import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;

    public Long createReview(Long userId, Long orderId, Long storeId, Integer rating, String content) {

        // 1. 주문조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        // 2. 가게조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 가게가 존재하지 않습니다."));
        // 3. 리뷰 작성 권한 확인
        if (!order.getUser().getId().equals(userId)) {
            throw new RuntimeException("본인의 주문이 아닙니다.");
        }
        // 4. 배달 상태 확인
        if (!order.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            throw new IllegalStateException("배달 완료된 주문만 리뷰 작성이 가능합니다.");
        }
        Review review = Review.create(order, order.getUser(), store, rating, content);

        reviewRepository.save(review);

        return review.getId();
    }
}
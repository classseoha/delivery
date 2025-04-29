package com.example.delivery.domain.review.service;

import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.review.dto.request.CreateReviewRequestDto;
import com.example.delivery.domain.review.dto.response.ReviewResponseDto;
import com.example.delivery.domain.review.entity.Review;
import com.example.delivery.domain.review.repository.ReviewRepository;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StoreRepository storeRepository;

    @Test
    void 리뷰_생성_성공() {
        // given(준비)
        Long orderId = 1L; // 주문 id = 1번 설정
        Long storeId = 2L; // 가게 id = 2번 설정
        Order order = mock(Order.class); // Mock 객체 (가짜 객체)
        Store store = mock(Store.class); // Mock 객체 (가짜 객체)
        User user = mock(User.class); // Mock 객체 (가짜 객체)

        // 고객이 2번 가게에 별점 5점 내용 "맛잇어요!" 라고 남긴다는 설정
        CreateReviewRequestDto requestDto = new CreateReviewRequestDto(storeId, 5, "맛있어요!");

        given(orderRepository.findByIdOrElseThrow(orderId)).willReturn(order); // 미리 만들어둔 oreder 객체 반환(Mock객체)
        given(storeRepository.findByIdOrElseThrow(storeId)).willReturn(store); // 미리 만들어둔 store 객체 반환(Mock객체)
        given(order.getOrderStatus()).willReturn(OrderStatus.DELIVERED); // 배달완료상태 설정
        given(order.getUser()).willReturn(user); //주문한 사람(user)을 넘김, 주문에 연결된 고객 정보를 설정


        /*
        save() 호출 시 넘어온 리뷰에 id = 100을 심어줌
        심어진 리뷰를 그대로 리턴
        -> 서비스 로직에서 review.getid()가 100으로 세팅돼 있게 함
        (원래 JPA에서는 save() 후 id가 자동으로 생기지만, 테스트에서는 직접 id를 세팅해줘야 합니다.)
         */
        given(reviewRepository.save(any(Review.class))).willAnswer(invocation -> {
                    Review savedReview = invocation.getArgument(0);
                    ReflectionTestUtils.setField(savedReview, "id", 100L);
                    return savedReview;
                });

        // when(행동)
        Long reviewId = reviewService.createReview(orderId, requestDto); //리뷰를 생성 (이 때 위에 세팅한 Mock들이 작동)

        // then(검증)
        assertNotNull(reviewId); //정상 동작했으면 reviewId가 null이 아니어야 함
    }

    @Test
    void 리뷰_조회_성공() {
        // given(준비)
        Long storeId = 1L; // 가게 1번 설정

        //평점 3~5 사이 리뷰를 조회하는 상황 가정
        Integer minRating = 3;
        Integer maxRating = 5;

        // review 객체를 가짜(Mock)로 생성
        Review review1 = mock(Review.class);
        Review review2 = mock(Review.class);

        // review1, review2를 묶어 하나의 리스트로 만듦
        List<Review> reviewList = List.of(review1, review2);

        //Mock 객체가 호출될 때 특정 결과를 리턴하도록 설정
        given(reviewRepository.findByStoreIdAndRatingBetweenOrderByCreatedAtDesc(storeId, minRating, maxRating))
                .willReturn(reviewList);


        // when(행동) //실제로 리뷰들을 가져오는 과정 수행 , 정상 동작하는지 검증
        List<ReviewResponseDto> result = reviewService.getReviews(storeId, minRating, maxRating); // 실제로 리뷰들을 가져오는 과정 수행 , 정상 동작하는지 검증

        // then(검증)
        assertNotNull(result); // result가 null이 아님을 검증
        assertEquals(2, result.size()); // 조회 결과 리스트의 크기가 2개인지 확인 , 아까 reviewList를 2개 넣었기 때문에, 변환 후에도 2개여야 함

    }
}

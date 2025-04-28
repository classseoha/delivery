package com.example.delivery.domain.Order.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.service.Order.OrderService;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.order.repository.CartRepository;
import com.example.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalTime;
import java.util.Optional;

public class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService; // 실제 테스트 대상 서비스 클래스

    private User mockUser;
    private Cart mockCart;
    private Store mockStore;

    @BeforeEach
    public void setUp() {
        // Mock 객체 초기화
        MockitoAnnotations.openMocks(this);

        // User 객체 초기화
        mockUser = new User();
        ReflectionTestUtils.setField(mockUser, "id", 1L);

        // Store 객체 초기화
        mockStore = new Store();
        ReflectionTestUtils.setField(mockStore, "openingTime", LocalTime.of(9, 0));
        ReflectionTestUtils.setField(mockStore, "closingTime", LocalTime.of(21, 0));

        // Cart 객체 초기화
        mockCart = new Cart(mockUser, mockStore);
        ReflectionTestUtils.setField(mockCart, "id", 1L); // Cart ID 설정
    }

    @Test
    public void testCreateOrder_UserNotFound() {
        // given
        Long userId = 1L;
        Long cartId = 1L;

        // User 조회 Mock 설정 (User가 없으면 예외 발생)
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.createOrder(userId, cartId);
        });
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void testCreateOrder_CartNotFound() {
        // given
        Long userId = 1L;
        Long cartId = 1L;

        // User와 Cart 조회 Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.createOrder(userId, cartId);
        });
        assertEquals(ErrorCode.CART_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    public void testCreateOrder_NoPermission() {
        // given
        Long userId = 1L;
        Long cartId = 1L;

        // Cart의 사용자 ID와 다르게 설정 (권한 없음)
        User anotherUser = new User();
        ReflectionTestUtils.setField(anotherUser, "id", 2L);
        ReflectionTestUtils.setField(mockCart, "user", anotherUser);

        // User와 Cart 조회 Mock 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.createOrder(userId, cartId);
        });
        assertEquals(ErrorCode.NO_PERMISSION, exception.getErrorCode());
    }

    @Test
    public void testCreateOrder_AlreadyOrdered() {
        // given
        Long userId = 1L;
        Long cartId = 1L;

        // 이미 주문이 존재하는 경우
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
        when(orderRepository.existsByCart(mockCart)).thenReturn(true);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.createOrder(userId, cartId);
        });
        assertEquals(ErrorCode.ALREADY_ORDERED, exception.getErrorCode());
    }

    @Test
    public void testCreateOrder_Success() {
        // given
        Long userId = 1L;
        Long cartId = 1L;

        // 정상적인 주문
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));
        when(orderRepository.existsByCart(mockCart)).thenReturn(false);

        // when
        orderService.createOrder(userId, cartId);

        // then
        verify(orderRepository, times(1)).save(any(Order.class));  // save 호출 확인
    }
}

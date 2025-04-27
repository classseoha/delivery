package com.example.delivery.domain.Order.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import java.time.LocalTime;

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
        mockUser.setId(1L);

        // Store 객체 초기화
        mockStore = new Store();
        mockStore.setOpeningTime(LocalTime.of(9, 0));  // 09:00 AM
        mockStore.setClosingTime(LocalTime.of(21, 0)); // 09:00 PM

        // Cart 객체 초기화
        mockCart = new Cart(mockUser, mockStore);
        mockCart.setId(1L);  // Cart ID 설정
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
        anotherUser.setId(2L);
        mockCart.setUser(anotherUser);

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
    public void testCreateOrder_StoreNotOpen() {
        // given
        Long userId = 1L;
        Long cartId = 1L;

        // 09:00 AM보다 이른 시간으로 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(mockCart));

        // 현 시간보다 일찍 주문하려는 경우
        LocalTime mockTime = LocalTime.of(8, 0); // 08:00 AM
        LocalTime now = mockTime;

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            orderService.createOrder(userId, cartId);
        });
        assertEquals(ErrorCode.STORE_NOT_OPEN, exception.getErrorCode());
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

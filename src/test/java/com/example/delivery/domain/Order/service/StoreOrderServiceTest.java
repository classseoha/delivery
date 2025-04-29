package com.example.delivery.domain.Order.service;


import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.base.NotFoundException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.menu.repository.MenuRepository;
import com.example.delivery.domain.order.dto.Response.Order.OrderStatusResponseDto;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.CartItemRepository;
import com.example.delivery.domain.order.repository.CartRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.order.service.Cart.CartService;
import com.example.delivery.domain.order.service.Order.StoreOrderService;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StoreOrderServiceTest {

    @InjectMocks
    private StoreOrderService storeOrderService;
    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private UserRepository userRepository;


    @Mock
    private OrderRepository orderRepository;

    private Order mockOrder;
    private Long storeId = 1L;
    private Long orderId = 100L;
    private Long userId = 10L;

    @BeforeEach
    void setUp(){

        Store store = new Store();
        ReflectionTestUtils.setField(store, "id", storeId);

        Cart cart = new Cart();
        ReflectionTestUtils.setField(cart, "store", store);

        mockOrder = new Order();
        ReflectionTestUtils.setField(mockOrder, "id", orderId);
        ReflectionTestUtils.setField(mockOrder, "cart", cart);
    }

    @Test
    public void changeOrderStatusSuccessRequested(){
        mockOrder.changeOrderStatus(OrderStatus.REQUESTED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        OrderStatusResponseDto response = storeOrderService.changeOrderStatus(10L, orderId, storeId);

        assertEquals(OrderStatus.ACCEPTED.name(), response.getOrderStatus());

    }

    @Test
    public void changeOrderStatusSuccessAccepted(){
        mockOrder.changeOrderStatus(OrderStatus.ACCEPTED);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

        OrderStatusResponseDto response = storeOrderService.changeOrderStatus(10L, orderId, storeId);

        assertEquals(OrderStatus.DELIVERING.name(), response.getOrderStatus());

    }

    @Test
    public void changeOrderStatusFail(){
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            storeOrderService.changeOrderStatus(userId, orderId, storeId);
        });

        assertEquals(ErrorCode.ORDER_NOT_FOUND, exception.getErrorCode());

    }


}


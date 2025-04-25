package com.example.delivery.domain.order.service.Order;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.dto.Response.Order.OrderDetailResponseDto;
import com.example.delivery.domain.order.dto.Response.Order.OrderResponseDto;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.CartItemRepository;
import com.example.delivery.domain.order.repository.CartRepository;
import com.example.delivery.domain.order.repository.OrderRepository;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    // 주문 생성: 카트 ID로 주문을 생성
    public void createOrder(Long userId, Long cartId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

        if (!cart.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        if (orderRepository.existsByCart(cart)) {
            throw new CustomException(ErrorCode.ALREADY_ORDERED);
        }

        Order order = new Order(user, cart);
        orderRepository.save(order);
    }

    // 주문 목록 조회: 사용자의 모든 주문 목록을 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getMyOrders(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return orderRepository.findAllByUser(user).stream()
                .map(order -> {
                    List<CartItem> cartItems = cartItemRepository.findByCart(order.getCart());
                    return OrderResponseDto.from(order, cartItems);
                })
                .toList();
    }

    // 주문 상세 조회: 주문 ID로 해당 주문의 상세 정보를 조회
    @Transactional(readOnly = true)
    public OrderDetailResponseDto getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        List<CartItem> cartItems = cartItemRepository.findByCart(order.getCart());
        return OrderDetailResponseDto.from(order, cartItems);
    }

    // 주문 상태 변경: 가게 주인만 주문 상태를 변경 가능
    @Transactional
    public void changeOrderStatus(Long userId, Long orderId, Long storeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        Long orderStoreId = order.getCart().getStore().getId();
        if (!orderStoreId.equals(storeId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        // 주문 상태 변경
        switch (order.getOrderStatus()) {
            case REQUESTED:
                order.changeOrderStatus(OrderStatus.ACCEPTED);
                break;
            case ACCEPTED:
                order.changeOrderStatus(OrderStatus.DELIVERING);
                break;
            case DELIVERING:
                order.changeOrderStatus(OrderStatus.DELIVERED);
                break;
            case DELIVERED:
            case REJECTED:
                throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
            default:
                throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }

    // 주문 거절: 가게 주인만 주문을 거절 가능
    @Transactional
    public void rejectOrder(Long userId, Long orderId, Long storeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        Long orderStoreId = order.getCart().getStore().getId();
        if (!orderStoreId.equals(storeId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        // 주문 상태가 REQUESTED일 때만 거절 가능
        if (order.getOrderStatus() != OrderStatus.REQUESTED) {
            throw new CustomException(ErrorCode.ORDER_CANNOT_BE_REJECTED);
        }

        order.changeOrderStatus(OrderStatus.REJECTED);
    }
}
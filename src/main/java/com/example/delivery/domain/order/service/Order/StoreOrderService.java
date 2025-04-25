package com.example.delivery.domain.order.service.Order;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.dto.Response.Order.OrderStatusResponseDto;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.order.entity.OrderStatus;
import com.example.delivery.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreOrderService {

    private final OrderRepository orderRepository;

    public OrderStatusResponseDto changeOrderStatus(Long userId, Long orderId, Long storeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        validateStorePermission(order, storeId);

        // 상태 변경
        OrderStatus updatedStatus = updateOrderStatus(order);

        return OrderStatusResponseDto.from(order.getId(), updatedStatus.name());
    }

    public OrderStatusResponseDto rejectOrder(Long userId, Long orderId, Long storeId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        validateStorePermission(order, storeId);

        if (order.getOrderStatus() != OrderStatus.REQUESTED) {
            throw new CustomException(ErrorCode.ORDER_CANNOT_BE_REJECTED);
        }

        order.changeOrderStatus(OrderStatus.REJECTED);
        return OrderStatusResponseDto.from(order.getId(), OrderStatus.REJECTED.name());
    }

    private void validateStorePermission(Order order, Long storeId) {
        Long orderStoreId = order.getCart().getStore().getId();
        if (!orderStoreId.equals(storeId)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }
    }

    private OrderStatus updateOrderStatus(Order order) {
        switch (order.getOrderStatus()) {
            case REQUESTED:
                order.changeOrderStatus(OrderStatus.ACCEPTED);
                return OrderStatus.ACCEPTED;
            case ACCEPTED:
                order.changeOrderStatus(OrderStatus.DELIVERING);
                return OrderStatus.DELIVERING;
            case DELIVERING:
                order.changeOrderStatus(OrderStatus.DELIVERED);
                return OrderStatus.DELIVERED;
            case DELIVERED:
            case REJECTED:
            default:
                throw new CustomException(ErrorCode.INVALID_ORDER_STATUS);
        }
    }
}

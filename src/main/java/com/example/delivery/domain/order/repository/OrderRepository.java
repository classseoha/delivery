package com.example.delivery.domain.order.repository;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.Order;
import com.example.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 특정 유저의 모든 주문 목록 조회
    List<Order> findAllByUser(User user);

    // 특정 카트에 대한 주문이 존재하는지 확인 (카트 당 한 번만 주문 가능)
    boolean existsByCart(Cart cart);

    // 주문 ID로 주문 조회
    Optional<Order> findById(Long orderId);

    default Order findByIdOrElseThrow(Long id) {
        return findById(id).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }
}

package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 특정 장바구니에 담긴 모든 메뉴 항목을 리스트로 반환
    List<CartItem> findByCart(Cart cart);

    // 이미 담긴 메뉴 확인
    CartItem findByCartIdAndMenuId(Long cartId, Long menuId);
}
package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 특정 장바구니에 담긴 모든 메뉴 항목을 리스트로 반환
    List<CartItem> findByCart(Cart cart);

    // 장바구니에 이미 담긴 메뉴인지 확인
    Optional<CartItem> findByCartAndMenu(Cart cart, Menu menu);
}
package com.example.delivery.domain.order.repository;

import com.example.delivery.domain.order.entity.Cart;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // 유저의 장바구니 모두 리스트로 반환
    List<Cart> findAllByUser(User user);

    // 같은 메뉴 담겼는지 체크
    Optional<Cart> findByUserAndStore(User user, Store store);
}
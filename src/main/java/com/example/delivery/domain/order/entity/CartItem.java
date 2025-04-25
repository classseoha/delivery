package com.example.delivery.domain.order.entity;

import com.example.delivery.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cartitem")
@NoArgsConstructor
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private int quantity;

    public CartItem(Cart cart, Menu menu, int quantity) {
        this.cart = cart;
        this.menu = menu;
        this.quantity = quantity;
    }

    // 같은 메뉴를 담을 시 수량 증가
    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }
}

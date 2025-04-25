package com.example.delivery.domain.order.entity;

import com.example.delivery.domain.menu.entity.Menu;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cartitem")
@NoArgsConstructor
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
}

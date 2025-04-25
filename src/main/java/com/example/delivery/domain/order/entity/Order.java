package com.example.delivery.domain.order.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false, unique = true)
    private Cart cart;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus = OrderStatus.REQUESTED;
}

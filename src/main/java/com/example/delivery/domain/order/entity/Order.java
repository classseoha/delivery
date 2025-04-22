package com.example.delivery.domain.order.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order")
@Getter
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Order() {
    }

    public Order(String status, com.example.delivery.domain.user.entity.User user, com.example.delivery.domain.store.entity.Store store) {
        this.status = status;
        this.user = user;
        this.store = store;
    }
}

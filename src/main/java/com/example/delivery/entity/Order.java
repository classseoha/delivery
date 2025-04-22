package com.example.delivery.entity;

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

    public Order(String status, User user, Store store) {
        this.status = status;
        this.user = user;
        this.store = store;
    }
}

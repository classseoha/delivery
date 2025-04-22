package com.example.delivery.domain.store.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;

@Entity
@Table(name = "store")
@Getter
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String storeName;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime  closingTime;

    @Column(nullable = false)
    private Long minAmount;

    @Column(nullable = false)
    private String storeStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Store() {
    }

    public Store(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount, String storeStatus, com.example.delivery.domain.user.entity.User user) {
        this.storeName = storeName;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.minAmount = minAmount;
        this.storeStatus = storeStatus;
        this.user = user;
    }
}

package com.example.delivery.domain.store.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Table(name = "store")
@Getter
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StoreStatus storeStatus = StoreStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
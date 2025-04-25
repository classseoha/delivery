package com.example.delivery.domain.menu.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "menu")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(name ="status")
    private MenuStatus menuStatus = MenuStatus.ACTIVE;
}

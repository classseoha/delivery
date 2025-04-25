package com.example.delivery.domain.order.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Cart(User user, Store store) {
        this.user = user;
        this.store = store;
    }
}

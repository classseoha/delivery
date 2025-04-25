package com.example.delivery.domain.review.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = true, columnDefinition = "longtext")
    private String content;


    public Review(Order order, User user, Store store, Integer rating, String content) {
        this.order = order;
        this.user = user;
        this.store = store;
        this.rating = rating;
        this.content = content;
    }

    //정적 팩토리 메서드
    public static Review create(Order order, User user, Store store, Integer rating, String content) {
        return new Review(order, user, store, rating, content);
    }
}

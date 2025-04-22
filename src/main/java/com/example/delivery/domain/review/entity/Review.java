package com.example.delivery.domain.review.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "review")
@Getter
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating;

    @Column(nullable = true, columnDefinition = "longtext")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Review() {
    }

    public Review(Integer rating, String content, com.example.delivery.domain.user.entity.User user, Order order) {
        this.rating = rating;
        this.content = content;
        this.user = user;
        this.order = order;
    }
}

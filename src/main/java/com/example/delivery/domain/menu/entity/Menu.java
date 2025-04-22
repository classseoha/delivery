package com.example.delivery.domain.menu.entity;

import com.example.delivery.common.entity.BaseEntity;
import com.example.delivery.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "menu")
@Getter
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String menuName;

    @Column(nullable = false)
    private String intro;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String status;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public Menu() {
    }

    public Menu(String menuName, String intro, Integer price, String status, com.example.delivery.domain.store.entity.Store store) {
        this.menuName = menuName;
        this.intro = intro;
        this.price = price;
        this.status = status;
        this.store = store;
    }
}

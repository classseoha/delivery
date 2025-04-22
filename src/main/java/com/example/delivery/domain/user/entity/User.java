package com.example.delivery.domain.user.entity;

import com.example.delivery.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String authority;

    @Column(nullable = false)
    private String withdrawal;

    public User() {
    }

    public User(String email, String password, String address, String authority, String withdrawal) {
        this.email = email;
        this.password = password;
        this.address = address;
        this.authority = authority;
        this.withdrawal = withdrawal;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}

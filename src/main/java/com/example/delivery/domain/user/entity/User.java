package com.example.delivery.domain.user.entity;

import com.example.delivery.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
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

    @Enumerated(EnumType.STRING)
    @Column(name = "authority")
    private UserAuthority userAuthority = UserAuthority.USER;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isDeleted = true;

    public void updatePassword(String password) {
        this.password = password;
    }
}
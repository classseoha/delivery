package com.example.delivery.domain.user.entity;

import com.example.delivery.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@Where(clause = "is_active = 1") // 해당 엔티티에 대한 모든 조회에서 자동으로 활성 유저만 조회하는 조건 추가
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
    @Column(name = "authority", nullable = false)
    private UserAuthority userAuthority = UserAuthority.USER;

    @Column(name = "is_active", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean isActive = true;

    public void updatePassword(String newPassword) {

        this.password = newPassword;
    }

    public void updateAddress(String newAddress) {

        this.address = newAddress;
    }

    public void deleteUser() {

        this.isActive = false;  // isDeleted가 false(0)일 때 탈퇴한 상태
    }
}
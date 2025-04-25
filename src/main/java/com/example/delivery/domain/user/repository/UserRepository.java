package com.example.delivery.domain.user.repository;

import com.example.delivery.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 활성 유저만 조회하는 메서드
    List<User> findAllByIsActiveTrue();

    // 이메일로 유저 찾기 (활성 유저만)
    Optional<User> findByEmailAndIsActiveTrue(String email);

    // ID로 유저 찾기 (활성 유저만)
    Optional<User> findByIdAndIsActiveTrue(Long id);

    boolean existsByEmail(String email);

    default User findByIdOrElseThrow(Long id) {
        return findByIdAndIsActiveTrue(id).orElseThrow(() -> new EntityNotFoundException("해당 ID의 유저가 존재하지 않습니다."));
    }
}

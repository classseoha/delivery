package com.example.delivery.domain.user.repository;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.user.entity.User;
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
        return findByIdAndIsActiveTrue(id).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}

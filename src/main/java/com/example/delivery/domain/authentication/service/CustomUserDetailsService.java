package com.example.delivery.domain.authentication.service;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.authentication.CustomUserDetails;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService { // Spring Security가 로그인 시 사용자 인증 정보 불러오기 위해 호출하는 UserDetailsService 인터페이스 구현체

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { // 로그인 시 시큐리티가 이 메서드 자동 호출함
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다: " + email));

        // 시큐리티가 이해할 수 있는 CustomUserDetails 객체 반환
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserById(Long id) { // 로그인 시 시큐리티가 이 메서드 자동 호출함
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다."));

        // 시큐리티가 이해할 수 있는 CustomUserDetails 객체 반환
        return new CustomUserDetails(user); // 직접 만든 UserDetails 구현체
    }
}

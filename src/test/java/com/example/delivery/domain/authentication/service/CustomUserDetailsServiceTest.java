package com.example.delivery.domain.authentication.service;

import com.example.delivery.domain.user.entity.User;
import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.authentication.CustomUserDetails;
import com.example.delivery.domain.user.entity.UserAuthority;
import com.example.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailsServiceTest {

    private UserRepository userRepository;
    private CustomUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserById_success() {
        // given
        User user = new User("test@email.com", "password123", "서울", UserAuthority.USER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserById(1L);

        // then
        System.out.println("user.getEmail(): " + user.getEmail());
        System.out.println("userDetails.getUsername(): " + userDetails.getUsername());

        assertEquals("test@email.com", userDetails.getUser().getEmail());
        assertEquals("서울", user.getAddress());
        assertEquals(UserAuthority.USER, user.getUserAuthority());
    }

    @Test
    void loadUserById_notFound() {
        // given
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // when & then
        CustomException exception = assertThrows(CustomException.class, () ->
                userDetailsService.loadUserById(99L));
        assertEquals(ErrorCode.USER_NOT_FOUND.getMessage("사용자를 찾을 수 없습니다."), exception.getMessage());
    }
}
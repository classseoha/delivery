package com.example.delivery.domain.user.service;

import com.example.delivery.common.exception.base.CustomException;
import com.example.delivery.common.exception.enums.ErrorCode;
import com.example.delivery.domain.user.dto.SignUpRequestDto;
import com.example.delivery.domain.user.dto.UpdateUserRequestDto;
import com.example.delivery.domain.user.entity.User;
import com.example.delivery.domain.user.entity.UserAuthority;
import com.example.delivery.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("회원가입")
    class SignUpTest {

        @Test
        @DisplayName("성공")
        void signUp_success() {
            // given
            SignUpRequestDto requestDto = new SignUpRequestDto("test@email.com", "1234", "서울", UserAuthority.USER);
            when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
            when(passwordEncoder.encode("1234")).thenReturn("encoded1234");

            User savedUser = new User(requestDto.getEmail(), "encoded1234", requestDto.getAddress(), requestDto.getUserAuthority());
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            // when
            var response = userService.signUp(requestDto);

            // then
            assertEquals("test@email.com", response.getEmail());
            assertEquals("서울", response.getAddress());
            assertEquals(UserAuthority.USER, response.getUserAuthority());
        }

        @Test
        @DisplayName("이메일 형식 오류")
        void signUp_invalidEmail() {
            SignUpRequestDto requestDto = new SignUpRequestDto("invalidEmail", "1234", "서울", UserAuthority.USER);
            CustomException exception = assertThrows(CustomException.class, () -> userService.signUp(requestDto));
            assertEquals(ErrorCode.INVALID_PARAMETER, exception.getErrorCode());
        }

        @Test
        @DisplayName("이메일 중복")
        void signUp_duplicateEmail() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@email.com", "1234", "서울", UserAuthority.USER);
            when(userRepository.existsByEmail(requestDto.getEmail())).thenReturn(true);

            CustomException exception = assertThrows(CustomException.class, () -> userService.signUp(requestDto));
            assertEquals(ErrorCode.EXISTED_PARAMETER, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("회원 정보 수정")
    class UpdateUserTest {

        @Test
        @DisplayName("성공")
        void updateUser_success() {
            Long userId = 1L;
            User user = new User("test@email.com", "encoded1234", "서울", UserAuthority.USER);
            when(userRepository.findByIdAndIsActiveTrue(userId)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("1234", "encoded1234")).thenReturn(true);
            when(passwordEncoder.matches("newpass", "encoded1234")).thenReturn(false);
            when(passwordEncoder.encode("newpass")).thenReturn("encodedNewpass");

            UpdateUserRequestDto requestDto = new UpdateUserRequestDto("1234", "newpass", "부산");

            assertDoesNotThrow(() -> userService.updateUser(userId, requestDto));
        }

        @Test
        @DisplayName("비밀번호 불일치")
        void updateUser_wrongPassword() {
            Long userId = 1L;
            User user = new User("test@email.com", "encoded1234", "서울", UserAuthority.USER);
            when(userRepository.findByIdAndIsActiveTrue(userId)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrongpass", "encoded1234")).thenReturn(false);

            UpdateUserRequestDto requestDto = new UpdateUserRequestDto("wrongpass", "newpass", "부산");

            CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(userId, requestDto));
            assertEquals(ErrorCode.NOT_CORRECT_VALUE, exception.getErrorCode());
        }

        @Test
        @DisplayName("변경 내용 없음")
        void updateUser_noChanges() {
            Long userId = 1L;
            User user = new User("test@email.com", "encoded1234", "서울", UserAuthority.USER);
            when(userRepository.findByIdAndIsActiveTrue(userId)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("1234", "encoded1234")).thenReturn(true);

            UpdateUserRequestDto requestDto = new UpdateUserRequestDto("1234", "", "");

            CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(userId, requestDto));
            assertEquals(ErrorCode.NO_VALUE_CHANGED, exception.getErrorCode());
        }
    }

    @Nested
    @DisplayName("회원 탈퇴")
    class DeleteUserTest {

        @Test
        @DisplayName("성공")
        void deleteUser_success() {
            Long userId = 1L;
            User user = new User("test@email.com", "encoded1234", "서울", UserAuthority.USER);
            when(userRepository.findByIdAndIsActiveTrue(userId)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("1234", "encoded1234")).thenReturn(true);

            assertDoesNotThrow(() -> userService.deleteUser(userId, "1234"));
            assertFalse(user.isActive());
        }

        @Test
        @DisplayName("비밀번호 불일치")
        void deleteUser_wrongPassword() {
            Long userId = 1L;
            User user = new User("test@email.com", "encoded1234", "서울", UserAuthority.USER);
            when(userRepository.findByIdAndIsActiveTrue(userId)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrong", "encoded1234")).thenReturn(false);

            CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(userId, "wrong"));
            assertEquals(ErrorCode.NOT_CORRECT_VALUE, exception.getErrorCode());
        }
    }
}

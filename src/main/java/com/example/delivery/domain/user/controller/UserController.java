package com.example.delivery.domain.user.controller;

import com.example.delivery.domain.user.dto.DeleteUserRequestDto;
import com.example.delivery.domain.user.dto.SignUpRequestDto;
import com.example.delivery.domain.user.dto.SignUpResponseDto;
import com.example.delivery.domain.user.dto.UpdateUserRequestDto;
import com.example.delivery.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/delivery/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto requestDto) {

        SignUpResponseDto signUpResponseDto = userService.signUp(requestDto);

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody UpdateUserRequestDto requestDto) {

        // Spring Security가 관리하는 인증 정보(Authentication)를 가져오고 인증정보에서 현재 로그인한 사용자 이름을 꺼냄
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        userService.updateUser(userId, requestDto);

        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }

    @PatchMapping()
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequestDto requestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        userService.deleteUser(userId, requestDto.getPassword());

        return ResponseEntity.ok("회원 탈퇴 완료되었습니다.");
    }
}

package com.example.delivery.domain.user.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.user.dto.request.DeleteUserRequestDto;
import com.example.delivery.domain.user.dto.request.SignUpRequestDto;
import com.example.delivery.domain.user.dto.response.SignUpResponseDto;
import com.example.delivery.domain.user.dto.request.UpdateUserRequestDto;
import com.example.delivery.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponseDto<SignUpResponseDto>> signUp(@RequestBody SignUpRequestDto requestDto, HttpServletRequest request) {

        SignUpResponseDto signUpResponseDto = userService.signUp(requestDto);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.USER_CREATE_SUCCESS, signUpResponseDto, request.getRequestURI()));
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDto<Void>> updateUser(@RequestBody UpdateUserRequestDto requestDto, HttpServletRequest request) {

        // Spring Security가 관리하는 인증 정보(Authentication)를 가져오고 인증정보에서 현재 로그인한 사용자 이름을 꺼냄
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        userService.updateUser(userId, requestDto);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.USER_UPDATE_SUCCESS, null, request.getRequestURI()));
    }

    @PatchMapping()
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(@RequestBody DeleteUserRequestDto requestDto, HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        userService.deleteUser(userId, requestDto.getPassword());

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.USER_DELETE_SUCCESS, null, request.getRequestURI()));
    }
}

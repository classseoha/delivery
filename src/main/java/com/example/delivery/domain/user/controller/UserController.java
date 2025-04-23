package com.example.delivery.domain.user.controller;

import com.example.delivery.domain.user.dto.SignUpRequestDto;
import com.example.delivery.domain.user.dto.SignUpResponseDto;
import com.example.delivery.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpRequestDto> signUp(@RequestBody SignUpRequestDto requestDto) {

        SignUpResponseDto signUpResponseDto = userService.signUp(requestDto);

        return new ResponseEntity<>(signUpResponseDto, HttpStatus.CREATED);
    }


}

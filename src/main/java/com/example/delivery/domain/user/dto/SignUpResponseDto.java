package com.example.delivery.domain.user.dto;

import com.example.delivery.domain.user.entity.UserAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignUpResponseDto {

    private final String email;
    private final String address;
    private final UserAuthority userAuthority;
    private final LocalDateTime modifiedAt;
    
}

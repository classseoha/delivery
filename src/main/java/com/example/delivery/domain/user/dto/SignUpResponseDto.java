package com.example.delivery.domain.user.dto;

import com.example.delivery.domain.user.entity.UserAuthority;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignUpResponseDto {

    private final String email;
    private final String address;
    private final UserAuthority userAuthority;
    private final LocalDateTime modifiedAt;

    public SignUpResponseDto(String email, String address, UserAuthority userAuthority, LocalDateTime modifiedAt) {

        this.email = email;
        this.address = address;
        this.userAuthority = userAuthority;
        this.modifiedAt = modifiedAt;
    }
}

package com.example.delivery.domain.authentication.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class LoginRequestDto {

    private final String email;
    private final String password;

}

package com.example.delivery.domain.user.dto;

import com.example.delivery.domain.user.entity.UserAuthority;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class SignUpRequestDto {

    private final String email;
    private final String password;
    private final String address;
    private final UserAuthority userAuthority;

    @JsonCreator
    public SignUpRequestDto(
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("address") String address,
            @JsonProperty("authority") UserAuthority userAuthority
    ) {

        this.email = email;
        this.password = password;
        this.address = address;
        this.userAuthority = userAuthority;
    }
}

package com.example.delivery.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UpdateUserRequestDto {

    private final String currentPassword;
    private final String newPassword;
    private final String newAddress;

    @JsonCreator
    public UpdateUserRequestDto( // 기본생성자 없이 동작하도록 설정
            @JsonProperty("currentPassword") String currentPassword,
            @JsonProperty("newPassword") String newPassword,
            @JsonProperty("newAddress") String newAddress
    ) {

        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newAddress = newAddress;
    }
}

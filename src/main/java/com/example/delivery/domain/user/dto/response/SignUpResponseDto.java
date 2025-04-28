package com.example.delivery.domain.user.dto.response;

import com.example.delivery.domain.user.entity.UserAuthority;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SignUpResponseDto {

    private final String email;
    private final String address;
    private final UserAuthority userAuthority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime modifiedAt;
    
}

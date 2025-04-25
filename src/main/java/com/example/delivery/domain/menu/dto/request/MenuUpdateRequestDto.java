package com.example.delivery.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

    @NotBlank(message="메뉴 이름은 필수 입력 사항입니다!")
    private final String menuName;

    @NotBlank(message="메뉴 소개는 입력 사항입니다!")
    private final String intro;

    @NotNull(message="메뉴 가격은 필수 입력 사항입니다!")
    private final int price;

}

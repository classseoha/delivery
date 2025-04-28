package com.example.delivery.domain.menu.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MenuRequestDto {


    private final Long storeId;

    @NotBlank(message="메뉴 이름은 필수 입력 사항입니다!")
    private final String menuName;

    @NotBlank(message="메뉴 소개는 입력 사항입니다!")
    private final String intro;

    @NotNull(message="메뉴 가격은 필수 입력 사항입니다!")
    private final Integer price;

    @JsonCreator
    public MenuRequestDto(
            @JsonProperty("storeId") Long storeId,
            @JsonProperty("menuName")String menuName,
            @JsonProperty("intro")String intro,
            @JsonProperty("price")Integer price) {
        this.storeId = storeId;
        this.menuName = menuName;
        this.intro = intro;
        this.price = price;
    }
}

package com.example.delivery.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuUpdateRequestDto {

    private final String menuName;

    private final String intro;

    private final Integer price;

}

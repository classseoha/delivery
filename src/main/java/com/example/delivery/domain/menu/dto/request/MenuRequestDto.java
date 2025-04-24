package com.example.delivery.domain.menu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuRequestDto {

    private final Long userId;

    private final Long storeId;

    private final String menuName;

    private final String intro;

    private final int price;

}

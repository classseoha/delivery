package com.example.delivery.domain.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MenuResponseDto {

    private final Long id;
    private final Long storeId;
    private final String menuName;
    private final String intro;
    private final Integer price;

}

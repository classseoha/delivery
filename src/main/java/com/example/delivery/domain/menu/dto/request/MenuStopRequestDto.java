package com.example.delivery.domain.menu.dto.request;

import com.example.delivery.domain.menu.entity.MenuStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuStopRequestDto {

    private final MenuStatus menuStatus;


}

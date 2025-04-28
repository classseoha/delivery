package com.example.delivery.domain.menu.dto.request;

import com.example.delivery.domain.menu.entity.MenuStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class MenuStopRequestDto {

    private final MenuStatus menuStatus;


    @JsonCreator
    public MenuStopRequestDto(
            @JsonProperty("menuStatus")MenuStatus menuStatus) {
        this.menuStatus = menuStatus;
    }
}

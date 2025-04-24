package com.example.delivery.domain.menu.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.menu.dto.request.MenuRequestDto;
import com.example.delivery.domain.menu.dto.response.MenuResponseDto;
import com.example.delivery.domain.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/delivery")
@RequiredArgsConstructor
public class MenuController {
    public final MenuService menuService;

    @PostMapping("/menus")
    public ResponseEntity<MenuResponseDto> save
            //Session 추가 예정
            (Long userId, @RequestBody MenuRequestDto menuRequestDto){

        MenuResponseDto menuResponseDto = menuService.save(userId, menuRequestDto);
        return ResponseEntity.ok(menuResponseDto);

    }

    @GetMapping("/stores/{storeId}/menus")
    public ResponseEntity<ApiResponseDto<List<MenuResponseDto>>> findByStore(@PathVariable Long storeId, HttpServletRequest request){
        List<MenuResponseDto> menuResponseDtoList = menuService.findByStore(storeId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_SUCCESS, menuResponseDtoList, request.getRequestURI()));

    }

    //@GetMapping("/stores/")

}

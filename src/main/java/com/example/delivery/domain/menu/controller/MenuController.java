package com.example.delivery.domain.menu.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.menu.dto.request.MenuRequestDto;
import com.example.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.delivery.domain.menu.dto.response.MenuResponseDto;
import com.example.delivery.domain.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class MenuController {
    public final MenuService menuService;

    @PostMapping("/store/{storeId}/menus")
    public ResponseEntity<ApiResponseDto<MenuResponseDto>> save
            //Session 추가 예정
            (Long userId, @PathVariable Long storeId,
             @RequestBody MenuRequestDto menuRequestDto, HttpServletRequest request){

        MenuResponseDto menuResponseDto = menuService.save(storeId, menuRequestDto);
        return ResponseEntity.ok(ApiResponseDto.success
                                (SuccessCode.CREATE_MENU_SUCCESS, menuResponseDto, request.getRequestURI()));

    }

    @GetMapping("/menus")
    public ResponseEntity<ApiResponseDto<List<MenuResponseDto>>>
                    findByStore(@PathVariable Long storeId, HttpServletRequest request){
        List<MenuResponseDto> menuResponseDtoList = menuService.findByStore(storeId);
        return ResponseEntity.ok(ApiResponseDto.success
                                (SuccessCode.GET_MENU_SUCCESS, menuResponseDtoList, request.getRequestURI()));

    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponseDto<MenuResponseDto>>
                    findOne(@PathVariable Long menuId, HttpServletRequest request){
        return ResponseEntity.ok(ApiResponseDto.success
                                (SuccessCode.GET_ONE_MENU_SUCCESS, menuService.findOne(menuId), request.getRequestURI()));
    }

    @PutMapping("/store/menus/{menuId}")
    public ResponseEntity<ApiResponseDto<MenuResponseDto>>
                    update(Long userId, @PathVariable Long menuId, MenuUpdateRequestDto menuUpdateRequestDto ,HttpServletRequest request){

        MenuResponseDto menuResponseDto = menuService.update(userId, menuId, menuUpdateRequestDto);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.UPDATE_MENU_SUCCESS, menuResponseDto, request.getRequestURI()));

    }


}

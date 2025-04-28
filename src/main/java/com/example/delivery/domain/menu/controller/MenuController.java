package com.example.delivery.domain.menu.controller;

import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.menu.dto.request.MenuRequestDto;
import com.example.delivery.domain.menu.dto.request.MenuStopRequestDto;
import com.example.delivery.domain.menu.dto.request.MenuUpdateRequestDto;
import com.example.delivery.domain.menu.dto.response.MenuResponseDto;
import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.entity.MenuStatus;
import com.example.delivery.domain.menu.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class MenuController {
    public final MenuService menuService;

    @PostMapping("/store/{storeId}/menus")
    public ResponseEntity<ApiResponseDto<MenuResponseDto>> save
            (@PathVariable Long storeId,
             @Valid @RequestBody MenuRequestDto menuRequestDto, HttpServletRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        MenuResponseDto menuResponseDto = menuService.save(userId, storeId, menuRequestDto);
        return ResponseEntity.ok(ApiResponseDto.success
                                (SuccessCode.CREATE_MENU_SUCCESS, menuResponseDto, request.getRequestURI()));

    }

    @GetMapping("/menus")
    public ResponseEntity<ApiResponseDto<List<MenuResponseDto>>>
                findByStore(@RequestBody MenuRequestDto menuRequestDto, HttpServletRequest request){
        List<MenuResponseDto> menuResponseDtoList = menuService.findByStore(menuRequestDto.getStoreId());
        return ResponseEntity.ok(ApiResponseDto.success
                                (SuccessCode.GET_MENU_SUCCESS, menuResponseDtoList, request.getRequestURI()));

    }

    @GetMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponseDto<MenuResponseDto>>
                    findOne(@PathVariable Long menuId, @RequestParam Long storeId, HttpServletRequest request){
        return ResponseEntity.
                ok(ApiResponseDto.success(SuccessCode.GET_ONE_MENU_SUCCESS,
                        menuService.findOne(menuId, storeId), request.getRequestURI()));
    }

    @PutMapping("/store/menus/{menuId}")
    public ResponseEntity<ApiResponseDto<MenuResponseDto>>
                    update(@PathVariable Long menuId,
                           @Valid @RequestBody MenuUpdateRequestDto menuUpdateRequestDto ,HttpServletRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());
        MenuResponseDto menuResponseDto = menuService.update(userId, menuId, menuUpdateRequestDto);

        return ResponseEntity.
                ok(ApiResponseDto.success(SuccessCode.UPDATE_MENU_SUCCESS,
                        menuResponseDto, request.getRequestURI()));

    }

    // DeleteMapping과 PutMapping 사이의 차이.
    // DeleteMapping이 좀 더 RESTful 하다는 의견.
    @PatchMapping("/store/menus/{menuId}/status")
    public ResponseEntity<ApiResponseDto<MenuStatus>>
                    changeStatusMenu(@PathVariable Long menuId, HttpServletRequest request){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        System.out.println(userId);


        menuService.changeStatusMenu(userId, menuId);
        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.CHANGE_MENU_SUCCESS, null, request.getRequestURI()));

    }

}

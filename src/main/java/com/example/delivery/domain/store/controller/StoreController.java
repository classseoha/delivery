package com.example.delivery.domain.store.controller;


import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.store.dto.*;
import com.example.delivery.domain.store.service.StoreService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class StoreController {

    // Spring Security가 관리하는 인증 정보(Authentication)를 가져오고 인증정보에서 현재 로그인한 사용자 이름을 꺼냄
    //시큐리티 적용사항이니 모든 컨트롤러 메서드 내부에 적용시키시면 됩니다.



    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<ApiResponseDto<CreateResponseDto>> createStore( @RequestBody CreateRequestDto dto, HttpServletRequest request) {

        CreateResponseDto response = storeService.createStore(dto.getStoreName()
                , dto.getOpeningTime()
                , dto.getClosingTime()
                , dto.getMinAmount());

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.CREATE_SUCCESS, response, request.getRequestURI()));
    }


    @GetMapping("storelist") //전체 조회
    public ResponseEntity<ApiResponseDto<List<GetStoreResponseDto>>> findAll(HttpServletRequest request) {

        List<GetStoreResponseDto> findAllStore = storeService.findAll();

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_ONE_SUCCESS, findAllStore,request.getRequestURI()));
    }

    @PutMapping("/stores/{id}") //가게 수정
    public ResponseEntity<ApiResponseDto<UpdateResponseDto>> updateStore(@PathVariable Long id, @RequestBody UpdateRequestDto dto,HttpServletRequest request) {

        UpdateResponseDto updateResponseDto = storeService.updateStore(id
                , dto.getStoreName()
                , dto.getOpeningTime()
                , dto.getClosingTime()
                , dto.getMinAmount());

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DELETE_SUCCESS, updateResponseDto,request.getRequestURI()));
    }

    @DeleteMapping("/stores/{id}") // 가게 뿌시기
    public ResponseEntity<ApiResponseDto<Void>> deleteStore(@PathVariable Long id) {

        storeService.deleteStore(id);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DELETE_SUCCESS, null));
    }

}

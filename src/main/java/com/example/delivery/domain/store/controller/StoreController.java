package com.example.delivery.domain.store.controller;


import com.example.delivery.common.exception.enums.SuccessCode;
import com.example.delivery.common.response.ApiResponseDto;
import com.example.delivery.domain.store.dto.*;
import com.example.delivery.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    public ResponseEntity<ApiResponseDto<CreateResponseDto>> createStore(@RequestBody CreateRequestDto dto) {

        CreateResponseDto response = storeService.createStore(dto.getStoreName()
                , dto.getOpeningTime()
                , dto.getClosingTime()
                , dto.getMinAmount()
        );

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.CREATE_SUCCESS, response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<GetStoreResponseDto>> findById(@PathVariable Long id) {

        GetStoreResponseDto findById = storeService.findById(id);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.GET_SUCCESS, findById));
    }


    @GetMapping() //전체 조회
    public ResponseEntity<ApiResponseDto<List<GetStoreResponseDto>>> findAll() {

        List<GetStoreResponseDto> findAllStore = storeService.findAll();

        return ResponseEntity.ok(ApiResponseDto.successList(SuccessCode.GET_ONE_SUCCESS, findAllStore));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<UpdateResponseDto>> updateStore(@PathVariable Long id, @RequestBody UpdateRequestDto dto) {

        UpdateResponseDto updateResponseDto = storeService.updateStore(id
                , dto.getStoreName()
                , dto.getOpeningTime()
                , dto.getClosingTime()
                , dto.getMinAmount());


        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DELETE_SUCCESS, updateResponseDto));
    }


    @DeleteMapping("/{id}") // 가게 뿌시기
    public ResponseEntity<ApiResponseDto<Void>> deleteStore(@PathVariable Long id) {

        storeService.deleteStore(id);

        return ResponseEntity.ok(ApiResponseDto.success(SuccessCode.DELETE_SUCCESS, null));
    }

}

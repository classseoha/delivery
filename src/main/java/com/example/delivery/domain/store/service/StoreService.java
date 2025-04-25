package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.CreateResponseDto;
import com.example.delivery.domain.store.dto.GetStoreResponseDto;
import com.example.delivery.domain.store.dto.UpdateResponseDto;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StoreService {

    CreateResponseDto createStore(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount);

    List<GetStoreResponseDto> findAll();

    void deleteStore(Long id);

    UpdateResponseDto updateStore(Long id, String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount);


}

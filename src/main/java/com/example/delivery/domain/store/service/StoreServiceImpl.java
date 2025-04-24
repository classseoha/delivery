package com.example.delivery.domain.store.service;

import com.example.delivery.domain.store.dto.CreateResponseDto;
import com.example.delivery.domain.store.dto.GetStoreResponseDto;
import com.example.delivery.domain.store.dto.UpdateResponseDto;
import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private final StoreRepository storeRepository;


    @Override //가게 오픈
    public CreateResponseDto createStore(String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount, String storeStatus) {

        Store store = new Store(storeName, openingTime, closingTime, minAmount, storeStatus);

        Store savedStore = storeRepository.save(store);

        return new CreateResponseDto(savedStore.getId()
                , savedStore.getStoreName()
                , savedStore.getOpeningTime()
                , savedStore.getClosingTime()
                , savedStore.getMinAmount()
                , savedStore.getStoreStatus());
    }

    @Override //가게 단일 조회
    public GetStoreResponseDto findById(Long id) {

        Store findById = storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return new GetStoreResponseDto(findById.getId()
                , findById.getStoreName()
                , findById.getOpeningTime()
                , findById.getClosingTime()
                , findById.getMinAmount()
                , findById.getStoreStatus());
    }

    @Override // 가게 전체 조회
    public List<GetStoreResponseDto> findAll() {

        List<Store> findAllStoreList = storeRepository.findAll();

        List<GetStoreResponseDto> responseDtoList = new ArrayList<>();
        for (Store stores : findAllStoreList) {

            GetStoreResponseDto getStoreResponseDto = new GetStoreResponseDto(stores.getId()
                    , stores.getStoreName()
                    , stores.getOpeningTime()
                    , stores.getClosingTime()
                    , stores.getMinAmount()
                    , stores.getStoreStatus());

            responseDtoList.add(getStoreResponseDto);
        }
        return responseDtoList;
    }

    @Override
    public void deleteStore(Long id) {

        storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        storeRepository.deleteById(id);
    }

    @Override
    public UpdateResponseDto updateStore(Long id, String storeName, LocalTime openingTime, LocalTime closingTime, Long minAmount) {

        Store updateStore = storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        updateStore.update(storeName, openingTime, closingTime, minAmount);

       return new UpdateResponseDto(updateStore.getStoreName(),updateStore.getOpeningTime(),updateStore.getClosingTime(),updateStore.getMinAmount(),updateStore.getStoreStatus());

    }
}

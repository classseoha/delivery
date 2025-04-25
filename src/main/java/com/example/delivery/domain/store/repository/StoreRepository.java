package com.example.delivery.domain.store.repository;

import com.example.delivery.domain.store.entity.Store;
import com.example.delivery.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findStoreByStoreName(String storeName);

    Long countStoresByUser(User user);

}

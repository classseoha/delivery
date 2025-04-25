package com.example.delivery.domain.menu.repository;

import com.example.delivery.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {

   Optional <List<Menu>> findByStoreId(Long storeId);
}

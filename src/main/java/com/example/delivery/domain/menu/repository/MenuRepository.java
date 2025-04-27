package com.example.delivery.domain.menu.repository;

import com.example.delivery.domain.menu.entity.Menu;
import com.example.delivery.domain.menu.entity.MenuStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

   Optional <List<Menu>> findByStoreIdAndMenuStatus(Long storeId, MenuStatus status);
   Optional <Menu> findByIdAndMenuStatus(Long id, MenuStatus status);
}

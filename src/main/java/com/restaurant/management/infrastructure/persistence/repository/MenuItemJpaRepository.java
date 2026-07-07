package com.restaurant.management.infrastructure.persistence.repository;

import com.restaurant.management.infrastructure.persistence.entity.MenuItemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, Long> {

    List<MenuItemJpaEntity> findByRestaurant_Id(Long restaurantId);
}
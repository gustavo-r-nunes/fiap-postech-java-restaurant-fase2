package com.restaurant.management.infrastructure.persistence.repository;

import com.restaurant.management.infrastructure.persistence.entity.RestaurantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, Long> {
}
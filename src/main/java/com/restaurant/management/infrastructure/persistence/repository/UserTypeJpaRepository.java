package com.restaurant.management.infrastructure.persistence.repository;

import com.restaurant.management.infrastructure.persistence.entity.UserTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserTypeJpaRepository extends JpaRepository<UserTypeJpaEntity, Long> {

    Optional<UserTypeJpaEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
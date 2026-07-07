package com.restaurant.management.application.gateway;

import com.restaurant.management.domain.entity.UserType;

import java.util.List;
import java.util.Optional;

public interface UserTypeGateway {

    UserType save(UserType userType);

    Optional<UserType> findById(Long id);

    Optional<UserType> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<UserType> findAll();

    void deleteById(Long id);
}
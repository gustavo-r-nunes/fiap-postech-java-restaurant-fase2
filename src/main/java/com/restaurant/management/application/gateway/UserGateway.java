package com.restaurant.management.application.gateway;

import com.restaurant.management.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserGateway {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);
}
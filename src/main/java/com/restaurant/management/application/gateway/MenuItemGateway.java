package com.restaurant.management.application.gateway;

import com.restaurant.management.domain.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemGateway {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    List<MenuItem> findAll();

    List<MenuItem> findByRestaurantId(Long restaurantId);

    void deleteById(Long id);
}
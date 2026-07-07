package com.restaurant.management.application.gateway;

import com.restaurant.management.domain.entity.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantGateway {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    void deleteById(Long id);
}
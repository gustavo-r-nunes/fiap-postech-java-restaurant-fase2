package com.restaurant.management.application.dto.input;

public record UpdateRestaurantInputData(
        Long id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId
) {
}
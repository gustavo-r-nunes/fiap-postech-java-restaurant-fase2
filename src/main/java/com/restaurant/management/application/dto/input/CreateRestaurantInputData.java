package com.restaurant.management.application.dto.input;

public record CreateRestaurantInputData(
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId
) {
}
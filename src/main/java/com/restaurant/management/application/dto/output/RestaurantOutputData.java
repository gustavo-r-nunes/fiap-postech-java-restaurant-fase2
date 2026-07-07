package com.restaurant.management.application.dto.output;

public record RestaurantOutputData(
        Long id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId,
        String ownerName
) {
}
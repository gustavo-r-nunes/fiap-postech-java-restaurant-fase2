package com.restaurant.management.presentation.dto.response;

public record RestaurantResponse(
        Long id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId,
        String ownerName
) {
}
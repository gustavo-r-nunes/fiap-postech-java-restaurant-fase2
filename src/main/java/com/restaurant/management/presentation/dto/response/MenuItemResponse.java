package com.restaurant.management.presentation.dto.response;

import java.math.BigDecimal;

public record MenuItemResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean onlyAvailableInRestaurant,
        String photoPath,
        Long restaurantId,
        String restaurantName
) {
}
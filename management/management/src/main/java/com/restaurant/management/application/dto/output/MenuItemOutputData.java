package com.restaurant.management.application.dto.output;

import java.math.BigDecimal;

public record MenuItemOutputData(
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
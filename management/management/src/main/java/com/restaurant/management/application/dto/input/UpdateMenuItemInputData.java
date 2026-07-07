package com.restaurant.management.application.dto.input;

import java.math.BigDecimal;

public record UpdateMenuItemInputData(
        Long id,
        String name,
        String description,
        BigDecimal price,
        boolean onlyAvailableInRestaurant,
        String photoPath,
        Long restaurantId
) {
}
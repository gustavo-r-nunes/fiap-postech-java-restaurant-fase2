package com.restaurant.management.presentation.mapper;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.presentation.dto.request.RestaurantRequest;

public class RestaurantPresentationMapper {

    private RestaurantPresentationMapper() {
    }

    public static CreateRestaurantInputData toCreateInputData(RestaurantRequest request) {
        return new CreateRestaurantInputData(
                request.name(),
                request.address(),
                request.cuisineType(),
                request.openingHours(),
                request.ownerId()
        );
    }

    public static UpdateRestaurantInputData toUpdateInputData(Long id, RestaurantRequest request) {
        return new UpdateRestaurantInputData(
                id,
                request.name(),
                request.address(),
                request.cuisineType(),
                request.openingHours(),
                request.ownerId()
        );
    }
}
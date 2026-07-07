package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;

import java.util.List;

public interface RestaurantUseCase {

    RestaurantOutputData create(CreateRestaurantInputData inputData);

    List<RestaurantOutputData> findAll();

    RestaurantOutputData findById(Long id);

    RestaurantOutputData update(UpdateRestaurantInputData inputData);

    void delete(Long id);
}
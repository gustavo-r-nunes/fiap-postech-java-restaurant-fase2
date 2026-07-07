package com.restaurant.management.presentation.presenter;

import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.presentation.dto.response.RestaurantResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RestaurantRestPresenter {

    public RestaurantResponse present(RestaurantOutputData outputData) {
        return new RestaurantResponse(
                outputData.id(),
                outputData.name(),
                outputData.address(),
                outputData.cuisineType(),
                outputData.openingHours(),
                outputData.ownerId(),
                outputData.ownerName()
        );
    }

    public List<RestaurantResponse> present(List<RestaurantOutputData> outputDataList) {
        return outputDataList.stream()
                .map(this::present)
                .toList();
    }
}
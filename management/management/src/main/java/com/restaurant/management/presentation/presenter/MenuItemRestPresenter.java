package com.restaurant.management.presentation.presenter;

import com.restaurant.management.application.dto.output.MenuItemOutputData;
import com.restaurant.management.presentation.dto.response.MenuItemResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuItemRestPresenter {

    public MenuItemResponse present(MenuItemOutputData outputData) {
        return new MenuItemResponse(
                outputData.id(),
                outputData.name(),
                outputData.description(),
                outputData.price(),
                outputData.onlyAvailableInRestaurant(),
                outputData.photoPath(),
                outputData.restaurantId(),
                outputData.restaurantName()
        );
    }

    public List<MenuItemResponse> present(List<MenuItemOutputData> outputDataList) {
        return outputDataList.stream()
                .map(this::present)
                .toList();
    }
}
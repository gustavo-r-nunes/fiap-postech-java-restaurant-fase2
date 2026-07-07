package com.restaurant.management.presentation.mapper;

import com.restaurant.management.application.dto.input.CreateMenuItemInputData;
import com.restaurant.management.application.dto.input.UpdateMenuItemInputData;
import com.restaurant.management.presentation.dto.request.MenuItemRequest;

public class MenuItemPresentationMapper {

    private MenuItemPresentationMapper() {
    }

    public static CreateMenuItemInputData toCreateInputData(MenuItemRequest request) {
        return new CreateMenuItemInputData(
                request.name(),
                request.description(),
                request.price(),
                request.onlyAvailableInRestaurant(),
                request.photoPath(),
                request.restaurantId()
        );
    }

    public static UpdateMenuItemInputData toUpdateInputData(Long id, MenuItemRequest request) {
        return new UpdateMenuItemInputData(
                id,
                request.name(),
                request.description(),
                request.price(),
                request.onlyAvailableInRestaurant(),
                request.photoPath(),
                request.restaurantId()
        );
    }
}
package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateMenuItemInputData;
import com.restaurant.management.application.dto.input.UpdateMenuItemInputData;
import com.restaurant.management.application.dto.output.MenuItemOutputData;

import java.util.List;

public interface MenuItemUseCase {

    MenuItemOutputData create(CreateMenuItemInputData inputData);

    List<MenuItemOutputData> findAll();

    MenuItemOutputData findById(Long id);

    List<MenuItemOutputData> findByRestaurantId(Long restaurantId);

    MenuItemOutputData update(UpdateMenuItemInputData inputData);

    void delete(Long id);
}
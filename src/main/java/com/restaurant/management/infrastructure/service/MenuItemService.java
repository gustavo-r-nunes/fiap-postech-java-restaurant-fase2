package com.restaurant.management.infrastructure.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.management.application.dto.input.CreateMenuItemInputData;
import com.restaurant.management.application.dto.input.UpdateMenuItemInputData;
import com.restaurant.management.application.dto.output.MenuItemOutputData;
import com.restaurant.management.application.gateway.MenuItemGateway;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.application.usecase.MenuItemInteractor;
import com.restaurant.management.application.usecase.MenuItemUseCase;


@Service
public class MenuItemService implements MenuItemUseCase{
    private final MenuItemInteractor interactor;

    public MenuItemService(MenuItemGateway menuItemGateway, RestaurantGateway restaurantGateway){
        this.interactor = new MenuItemInteractor(menuItemGateway, restaurantGateway);
    }

    @Override
    @Transactional
    public MenuItemOutputData create(CreateMenuItemInputData inputData){
        return interactor.create(inputData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemOutputData> findAll() {
        return interactor.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemOutputData findById(Long id) {
        return interactor.findById(id);
    }

    @Override
    @Transactional
    public MenuItemOutputData update(UpdateMenuItemInputData inputData){
        return interactor.update(inputData);
    }

    @Override
    @Transactional
    public void delete(Long id){
        interactor.delete(id);
    }

    @Override
    public List<MenuItemOutputData> findByRestaurantId(Long restaurantId) {
        return interactor.findByRestaurantId(restaurantId);
    }
}

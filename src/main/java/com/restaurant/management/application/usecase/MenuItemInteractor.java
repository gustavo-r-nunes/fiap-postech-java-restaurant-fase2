package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateMenuItemInputData;
import com.restaurant.management.application.dto.input.UpdateMenuItemInputData;
import com.restaurant.management.application.dto.output.MenuItemOutputData;
import com.restaurant.management.application.gateway.MenuItemGateway;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.domain.entity.MenuItem;
import com.restaurant.management.domain.entity.Restaurant;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuItemInteractor implements MenuItemUseCase {

    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public MenuItemInteractor(
            MenuItemGateway menuItemGateway,
            RestaurantGateway restaurantGateway
    ) {
        this.menuItemGateway = menuItemGateway;
        this.restaurantGateway = restaurantGateway;
    }

    @Override
    @Transactional
    public MenuItemOutputData create(CreateMenuItemInputData inputData) {
        Restaurant restaurant = restaurantGateway.findById(inputData.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        MenuItem menuItem = new MenuItem(
                inputData.name(),
                inputData.description(),
                inputData.price(),
                inputData.onlyAvailableInRestaurant(),
                inputData.photoPath(),
                restaurant
        );

        return toOutputData(menuItemGateway.save(menuItem));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemOutputData> findAll() {
        return menuItemGateway.findAll()
                .stream()
                .map(this::toOutputData)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemOutputData findById(Long id) {
        MenuItem menuItem = menuItemGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado"));

        return toOutputData(menuItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemOutputData> findByRestaurantId(Long restaurantId) {
        restaurantGateway.findById(restaurantId)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        return menuItemGateway.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toOutputData)
                .toList();
    }

    @Override
    @Transactional
    public MenuItemOutputData update(UpdateMenuItemInputData inputData) {
        MenuItem menuItem = menuItemGateway.findById(inputData.id())
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado"));

        Restaurant restaurant = restaurantGateway.findById(inputData.restaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        menuItem.setName(inputData.name());
        menuItem.setDescription(inputData.description());
        menuItem.setPrice(inputData.price());
        menuItem.setOnlyAvailableInRestaurant(inputData.onlyAvailableInRestaurant());
        menuItem.setPhotoPath(inputData.photoPath());
        menuItem.setRestaurant(restaurant);

        return toOutputData(menuItemGateway.save(menuItem));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        menuItemGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do cardápio não encontrado"));

        menuItemGateway.deleteById(id);
    }

    private MenuItemOutputData toOutputData(MenuItem menuItem) {
        return new MenuItemOutputData(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isOnlyAvailableInRestaurant(),
                menuItem.getPhotoPath(),
                menuItem.getRestaurant().getId(),
                menuItem.getRestaurant().getName()
        );
    }
}
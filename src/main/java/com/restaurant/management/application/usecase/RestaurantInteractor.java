package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.domain.entity.Restaurant;
import com.restaurant.management.domain.entity.User;
import com.restaurant.management.domain.exception.BusinessRuleException;
import com.restaurant.management.domain.exception.ResourceNotFoundException;

import java.util.List;

public class RestaurantInteractor implements RestaurantUseCase {

    private static final String RESTAURANT_OWNER_TYPE = "Dono de Restaurante";

    private final RestaurantGateway restaurantGateway;
    private final UserGateway userGateway;

    public RestaurantInteractor(
            RestaurantGateway restaurantGateway,
            UserGateway userGateway
    ) {
        this.restaurantGateway = restaurantGateway;
        this.userGateway = userGateway;
    }

    @Override
    public RestaurantOutputData create(CreateRestaurantInputData inputData) {
        User owner = userGateway.findById(inputData.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        validateOwner(owner);

        Restaurant restaurant = new Restaurant(
                inputData.name(),
                inputData.address(),
                inputData.cuisineType(),
                inputData.openingHours(),
                owner
        );

        return toOutputData(restaurantGateway.save(restaurant));
    }

    @Override
    public List<RestaurantOutputData> findAll() {
        return restaurantGateway.findAll()
                .stream()
                .map(this::toOutputData)
                .toList();
    }

    @Override
    public RestaurantOutputData findById(Long id) {
        Restaurant restaurant = restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        return toOutputData(restaurant);
    }

    @Override
    public RestaurantOutputData update(UpdateRestaurantInputData inputData) {
        Restaurant restaurant = restaurantGateway.findById(inputData.id())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        User owner = userGateway.findById(inputData.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        validateOwner(owner);

        restaurant.setName(inputData.name());
        restaurant.setAddress(inputData.address());
        restaurant.setCuisineType(inputData.cuisineType());
        restaurant.setOpeningHours(inputData.openingHours());
        restaurant.setOwner(owner);

        return toOutputData(restaurantGateway.save(restaurant));
    }

    @Override
    public void delete(Long id) {
        restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        restaurantGateway.deleteById(id);
    }

    private RestaurantOutputData toOutputData(Restaurant restaurant) {
        if (restaurant.getOwner() == null) {
            throw new IllegalStateException("Restaurante sem proprietário vinculado");
        }
        return new RestaurantOutputData(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getOwner().getId(),
                restaurant.getOwner().getName()
        );
    }

    private void validateOwner(User owner) {
        if (owner.getUserType() == null ||
                owner.getUserType().getName() == null ||
                !RESTAURANT_OWNER_TYPE.equalsIgnoreCase(owner.getUserType().getName())) {
            throw new BusinessRuleException("O usuário informado deve ser do tipo Dono de Restaurante");
        }
    }
}
package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.domain.entity.Restaurant;
import com.restaurant.management.domain.entity.User;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
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
    @Transactional
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
    @Transactional(readOnly = true)
    public List<RestaurantOutputData> findAll() {
        return restaurantGateway.findAll()
                .stream()
                .map(this::toOutputData)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantOutputData findById(Long id) {
        Restaurant restaurant = restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        return toOutputData(restaurant);
    }

    @Override
    @Transactional
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
    @Transactional
    public void delete(Long id) {
        restaurantGateway.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurante não encontrado"));

        restaurantGateway.deleteById(id);
    }

    private RestaurantOutputData toOutputData(Restaurant restaurant) {
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
            throw new IllegalArgumentException("O usuário informado deve ser do tipo Dono de Restaurante");
        }
    }
}
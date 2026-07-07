package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.application.gateway.UserGateway;
import com.restaurant.management.domain.entity.Restaurant;
import com.restaurant.management.domain.entity.User;
import com.restaurant.management.domain.entity.UserType;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantInteractorTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private RestaurantInteractor restaurantInteractor;

    @Test
    void shouldCreateRestaurantWhenOwnerIsRestaurantOwner() {
        UserType ownerType = new UserType("Dono de Restaurante");
        User owner = new User("João", "joao@email.com", ownerType);

        CreateRestaurantInputData inputData = new CreateRestaurantInputData(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                1L
        );

        when(userGateway.findById(1L)).thenReturn(Optional.of(owner));
        when(restaurantGateway.save(any(Restaurant.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantOutputData result = restaurantInteractor.create(inputData);

        assertEquals("Cantina Bella", result.name());
        assertEquals("Italiana", result.cuisineType());
        assertEquals("João", result.ownerName());

        verify(userGateway).findById(1L);
        verify(restaurantGateway).save(any(Restaurant.class));
    }

    @Test
    void shouldNotCreateRestaurantWhenUserIsClient() {
        UserType clientType = new UserType("Cliente");
        User client = new User("Maria", "maria@email.com", clientType);

        CreateRestaurantInputData inputData = new CreateRestaurantInputData(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                1L
        );

        when(userGateway.findById(1L)).thenReturn(Optional.of(client));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> restaurantInteractor.create(inputData)
        );

        assertEquals("O usuário informado deve ser do tipo Dono de Restaurante", exception.getMessage());

        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowWhenOwnerNotFoundOnCreate() {
        CreateRestaurantInputData inputData = new CreateRestaurantInputData(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                99L
        );

        when(userGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantInteractor.create(inputData));

        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void shouldFindRestaurantById() {
        UserType ownerType = new UserType("Dono de Restaurante");
        User owner = new User("João", "joao@email.com", ownerType);

        Restaurant restaurant = new Restaurant(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                owner
        );

        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));

        RestaurantOutputData result = restaurantInteractor.findById(1L);

        assertEquals("Cantina Bella", result.name());
        assertEquals("João", result.ownerName());

        verify(restaurantGateway).findById(1L);
    }

    @Test
    void shouldThrowWhenRestaurantNotFound() {
        when(restaurantGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> restaurantInteractor.findById(99L));
    }

    @Test
    void shouldFindAllRestaurants() {
        UserType ownerType = new UserType("Dono de Restaurante");
        User owner = new User("João", "joao@email.com", ownerType);

        Restaurant restaurant = new Restaurant(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                owner
        );

        when(restaurantGateway.findAll()).thenReturn(List.of(restaurant));

        List<RestaurantOutputData> result = restaurantInteractor.findAll();

        assertEquals(1, result.size());
        assertEquals("Cantina Bella", result.get(0).name());

        verify(restaurantGateway).findAll();
    }

    @Test
    void shouldUpdateRestaurantWhenOwnerIsValid() {
        UserType ownerType = new UserType("Dono de Restaurante");
        User oldOwner = new User("João", "joao@email.com", ownerType);
        User newOwner = new User("Carlos", "carlos@email.com", ownerType);

        Restaurant restaurant = new Restaurant(
                "Cantina Antiga",
                "Rua A",
                "Italiana",
                "10h às 20h",
                oldOwner
        );

        UpdateRestaurantInputData inputData = new UpdateRestaurantInputData(
                1L,
                "Cantina Nova",
                "Rua B",
                "Brasileira",
                "11h às 23h",
                2L
        );

        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(userGateway.findById(2L)).thenReturn(Optional.of(newOwner));
        when(restaurantGateway.save(any(Restaurant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RestaurantOutputData result = restaurantInteractor.update(inputData);

        assertEquals("Cantina Nova", result.name());
        assertEquals("Rua B", result.address());
        assertEquals("Brasileira", result.cuisineType());
        assertEquals("11h às 23h", result.openingHours());
        assertEquals("Carlos", result.ownerName());

        verify(restaurantGateway).save(restaurant);
    }

    @Test
    void shouldDeleteRestaurant() {
        UserType ownerType = new UserType("Dono de Restaurante");
        User owner = new User("João", "joao@email.com", ownerType);

        Restaurant restaurant = new Restaurant(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                owner
        );

        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantInteractor.delete(1L);

        verify(restaurantGateway).findById(1L);
        verify(restaurantGateway).deleteById(1L);
    }
}
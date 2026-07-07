package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateMenuItemInputData;
import com.restaurant.management.application.dto.input.UpdateMenuItemInputData;
import com.restaurant.management.application.dto.output.MenuItemOutputData;
import com.restaurant.management.application.gateway.MenuItemGateway;
import com.restaurant.management.application.gateway.RestaurantGateway;
import com.restaurant.management.domain.entity.MenuItem;
import com.restaurant.management.domain.entity.Restaurant;
import com.restaurant.management.domain.entity.User;
import com.restaurant.management.domain.entity.UserType;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemInteractorTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    @InjectMocks
    private MenuItemInteractor menuItemInteractor;

    @Test
    void shouldCreateMenuItemWhenRestaurantExists() {
        UserType ownerType = new UserType("Dono de Restaurante");
        User owner = new User("João", "joao@email.com", ownerType);
        Restaurant restaurant = new Restaurant(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                owner
        );

        CreateMenuItemInputData inputData = new CreateMenuItemInputData(
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                1L
        );

        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItemOutputData result = menuItemInteractor.create(inputData);

        assertEquals("Pizza Margherita", result.name());
        assertEquals(new BigDecimal("49.90"), result.price());
        assertEquals("Cantina Bella", result.restaurantName());

        verify(restaurantGateway).findById(1L);
        verify(menuItemGateway).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowWhenRestaurantNotFoundOnCreate() {
        CreateMenuItemInputData inputData = new CreateMenuItemInputData(
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                99L
        );

        when(restaurantGateway.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> menuItemInteractor.create(inputData));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void shouldFindMenuItemById() {
        Restaurant restaurant = createRestaurant();
        MenuItem menuItem = new MenuItem(
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                restaurant
        );

        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItemOutputData result = menuItemInteractor.findById(1L);

        assertEquals("Pizza Margherita", result.name());
        assertEquals("Cantina Bella", result.restaurantName());

        verify(menuItemGateway).findById(1L);
    }

    @Test
    void shouldFindAllMenuItems() {
        Restaurant restaurant = createRestaurant();
        MenuItem menuItem = new MenuItem(
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                restaurant
        );

        when(menuItemGateway.findAll()).thenReturn(List.of(menuItem));

        List<MenuItemOutputData> result = menuItemInteractor.findAll();

        assertEquals(1, result.size());
        assertEquals("Pizza Margherita", result.get(0).name());

        verify(menuItemGateway).findAll();
    }

    @Test
    void shouldFindMenuItemsByRestaurantId() {
        Restaurant restaurant = createRestaurant();
        MenuItem menuItem = new MenuItem(
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                restaurant
        );

        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByRestaurantId(1L)).thenReturn(List.of(menuItem));

        List<MenuItemOutputData> result = menuItemInteractor.findByRestaurantId(1L);

        assertEquals(1, result.size());
        assertEquals("Pizza Margherita", result.get(0).name());

        verify(restaurantGateway).findById(1L);
        verify(menuItemGateway).findByRestaurantId(1L);
    }

    @Test
    void shouldUpdateMenuItem() {
        Restaurant restaurant = createRestaurant();
        MenuItem menuItem = new MenuItem(
                "Pizza Antiga",
                "Descrição antiga",
                new BigDecimal("39.90"),
                true,
                "/old.jpg",
                restaurant
        );

        UpdateMenuItemInputData inputData = new UpdateMenuItemInputData(
                1L,
                "Pizza Nova",
                "Descrição nova",
                new BigDecimal("59.90"),
                false,
                "/new.jpg",
                1L
        );

        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem));
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.save(any(MenuItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MenuItemOutputData result = menuItemInteractor.update(inputData);

        assertEquals("Pizza Nova", result.name());
        assertEquals("Descrição nova", result.description());
        assertEquals(new BigDecimal("59.90"), result.price());
        assertEquals(false, result.onlyAvailableInRestaurant());
        assertEquals("/new.jpg", result.photoPath());

        verify(menuItemGateway).save(menuItem);
    }

    @Test
    void shouldDeleteMenuItem() {
        Restaurant restaurant = createRestaurant();
        MenuItem menuItem = new MenuItem(
                "Pizza Margherita",
                "Pizza com molho de tomate, mussarela e manjericão",
                new BigDecimal("49.90"),
                false,
                "/images/pizza-margherita.jpg",
                restaurant
        );

        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem));

        menuItemInteractor.delete(1L);

        verify(menuItemGateway).findById(1L);
        verify(menuItemGateway).deleteById(1L);
    }

    private Restaurant createRestaurant() {
        UserType ownerType = new UserType("Dono de Restaurante");
        User owner = new User("João", "joao@email.com", ownerType);

        return new Restaurant(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                owner
        );
    }
}
package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateMenuItemInputData;
import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateMenuItemInputData;
import com.restaurant.management.application.dto.output.MenuItemOutputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.infrastructure.persistence.repository.MenuItemJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import com.restaurant.management.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemInteractorIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MenuItemUseCase menuItemUseCase;

    @Autowired
    private RestaurantUseCase restaurantUseCase;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private UserTypeUseCase userTypeUseCase;

    MenuItemInteractorIntegrationTest(
            MenuItemJpaRepository menuItemRepository,
            RestaurantJpaRepository restaurantRepository,
            UserJpaRepository userRepository,
            UserTypeJpaRepository userTypeRepository
    ) {
        super(menuItemRepository, restaurantRepository, userRepository, userTypeRepository);
    }

    @Test
    void shouldCreateMenuItemUsingRealDatabase() {
        RestaurantOutputData restaurant = createRestaurant();

        MenuItemOutputData output = menuItemUseCase.create(
                new CreateMenuItemInputData(
                        "Pizza Margherita",
                        "Pizza com molho de tomate, mussarela e manjericão",
                        new BigDecimal("49.90"),
                        false,
                        "/images/pizza-margherita.jpg",
                        restaurant.id()
                )
        );

        assertNotNull(output.id());
        assertEquals("Pizza Margherita", output.name());
        assertEquals("Pizza com molho de tomate, mussarela e manjericão", output.description());
        assertEquals(0, new BigDecimal("49.90").compareTo(output.price()));
        assertFalse(output.onlyAvailableInRestaurant());
        assertEquals("/images/pizza-margherita.jpg", output.photoPath());
        assertEquals(restaurant.id(), output.restaurantId());
        assertEquals("Cantina Bella", output.restaurantName());
        assertTrue(menuItemRepository.existsById(output.id()));
    }

    @Test
    void shouldFindAllMenuItemsUsingRealDatabase() {
        RestaurantOutputData restaurant = createRestaurant();

        createMenuItem(restaurant.id(), "Pizza Margherita");
        createMenuItem(restaurant.id(), "Pizza Calabresa");

        List<MenuItemOutputData> result = menuItemUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindMenuItemByIdUsingRealDatabase() {
        RestaurantOutputData restaurant = createRestaurant();
        MenuItemOutputData created = createMenuItem(restaurant.id(), "Pizza Margherita");

        MenuItemOutputData found = menuItemUseCase.findById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("Pizza Margherita", found.name());
        assertEquals(restaurant.id(), found.restaurantId());
        assertEquals("Cantina Bella", found.restaurantName());
    }

    @Test
    void shouldFindMenuItemsByRestaurantIdUsingRealDatabase() {
        RestaurantOutputData restaurant = createRestaurant();

        createMenuItem(restaurant.id(), "Pizza Margherita");

        List<MenuItemOutputData> result = menuItemUseCase.findByRestaurantId(restaurant.id());

        assertEquals(1, result.size());
        assertEquals("Pizza Margherita", result.getFirst().name());
        assertEquals(restaurant.id(), result.getFirst().restaurantId());
    }

    @Test
    void shouldUpdateMenuItemUsingRealDatabase() {
        RestaurantOutputData restaurant = createRestaurant();
        MenuItemOutputData created = createMenuItem(restaurant.id(), "Pizza Margherita");

        MenuItemOutputData updated = menuItemUseCase.update(
                new UpdateMenuItemInputData(
                        created.id(),
                        "Pizza Calabresa",
                        "Pizza com calabresa, cebola e mussarela",
                        new BigDecimal("59.90"),
                        true,
                        "/images/pizza-calabresa.jpg",
                        restaurant.id()
                )
        );

        assertEquals(created.id(), updated.id());
        assertEquals("Pizza Calabresa", updated.name());
        assertEquals("Pizza com calabresa, cebola e mussarela", updated.description());
        assertEquals(0, new BigDecimal("59.90").compareTo(updated.price()));
        assertTrue(updated.onlyAvailableInRestaurant());
        assertEquals("/images/pizza-calabresa.jpg", updated.photoPath());
        assertEquals(restaurant.id(), updated.restaurantId());
    }

    @Test
    void shouldDeleteMenuItemUsingRealDatabase() {
        RestaurantOutputData restaurant = createRestaurant();
        MenuItemOutputData created = createMenuItem(restaurant.id(), "Pizza Margherita");

        menuItemUseCase.delete(created.id());

        assertFalse(menuItemRepository.existsById(created.id()));
    }

    @Test
    void shouldThrowWhenMenuItemDoesNotExist() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> menuItemUseCase.findById(999L)
        );
    }

    @Test
    void shouldThrowWhenCreatingMenuItemWithNonExistingRestaurant() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> menuItemUseCase.create(
                        new CreateMenuItemInputData(
                                "Pizza Margherita",
                                "Pizza com molho de tomate, mussarela e manjericão",
                                new BigDecimal("49.90"),
                                false,
                                "/images/pizza-margherita.jpg",
                                999L
                        )
                )
        );
    }

    @Test
    void shouldThrowWhenFindingMenuItemsByNonExistingRestaurant() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> menuItemUseCase.findByRestaurantId(999L)
        );
    }

    private MenuItemOutputData createMenuItem(Long restaurantId, String name) {
        return menuItemUseCase.create(
                new CreateMenuItemInputData(
                        name,
                        "Pizza com molho de tomate, mussarela e manjericão",
                        new BigDecimal("49.90"),
                        false,
                        "/images/pizza-margherita.jpg",
                        restaurantId
                )
        );
    }

    private RestaurantOutputData createRestaurant() {
        UserTypeOutputData ownerType = userTypeUseCase.create(
                new CreateUserTypeInputData("Dono de Restaurante")
        );

        UserOutputData owner = userUseCase.create(
                new CreateUserInputData(
                        "João Dono",
                        "joao.dono@email.com",
                        ownerType.id()
                )
        );

        return restaurantUseCase.create(
                new CreateRestaurantInputData(
                        "Cantina Bella",
                        "Rua das Flores, 123",
                        "Italiana",
                        "11h às 23h",
                        owner.id()
                )
        );
    }
}
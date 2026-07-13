package com.restaurant.management.application.usecase;

import com.restaurant.management.application.dto.input.CreateRestaurantInputData;
import com.restaurant.management.application.dto.input.CreateUserInputData;
import com.restaurant.management.application.dto.input.CreateUserTypeInputData;
import com.restaurant.management.application.dto.input.UpdateRestaurantInputData;
import com.restaurant.management.application.dto.output.RestaurantOutputData;
import com.restaurant.management.application.dto.output.UserOutputData;
import com.restaurant.management.application.dto.output.UserTypeOutputData;
import com.restaurant.management.domain.exception.ResourceNotFoundException;
import com.restaurant.management.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantInteractorIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private RestaurantUseCase restaurantUseCase;

    @Autowired
    private UserUseCase userUseCase;

    @Autowired
    private UserTypeUseCase userTypeUseCase;

    @Test
    void shouldCreateRestaurantUsingRealDatabase() {
        UserOutputData owner = createOwnerUser();

        RestaurantOutputData output = restaurantUseCase.create(
                new CreateRestaurantInputData(
                        "Cantina Bella",
                        "Rua das Flores, 123",
                        "Italiana",
                        "11h às 23h",
                        owner.id()
                )
        );

        assertNotNull(output.id());
        assertEquals("Cantina Bella", output.name());
        assertEquals("Rua das Flores, 123", output.address());
        assertEquals("Italiana", output.cuisineType());
        assertEquals("11h às 23h", output.openingHours());
        assertEquals(owner.id(), output.ownerId());
        assertEquals("João Dono", output.ownerName());
        assertTrue(restaurantRepository.existsById(output.id()));
    }

    @Test
    void shouldFindAllRestaurantsUsingRealDatabase() {
        UserOutputData owner = createOwnerUser();

        restaurantUseCase.create(new CreateRestaurantInputData(
                "Cantina Bella",
                "Rua das Flores, 123",
                "Italiana",
                "11h às 23h",
                owner.id()
        ));

        restaurantUseCase.create(new CreateRestaurantInputData(
                "Sabor Caseiro",
                "Avenida Central, 999",
                "Brasileira",
                "10h às 22h",
                owner.id()
        ));

        List<RestaurantOutputData> result = restaurantUseCase.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void shouldFindRestaurantByIdUsingRealDatabase() {
        UserOutputData owner = createOwnerUser();
        RestaurantOutputData created = createRestaurant(owner.id());

        RestaurantOutputData found = restaurantUseCase.findById(created.id());

        assertEquals(created.id(), found.id());
        assertEquals("Cantina Bella", found.name());
        assertEquals(owner.id(), found.ownerId());
        assertEquals("João Dono", found.ownerName());
    }

    @Test
    void shouldUpdateRestaurantUsingRealDatabase() {
        UserOutputData owner = createOwnerUser();
        RestaurantOutputData created = createRestaurant(owner.id());

        RestaurantOutputData updated = restaurantUseCase.update(
                new UpdateRestaurantInputData(
                        created.id(),
                        "Cantina Atualizada",
                        "Avenida Central, 999",
                        "Brasileira",
                        "10h às 22h",
                        owner.id()
                )
        );

        assertEquals(created.id(), updated.id());
        assertEquals("Cantina Atualizada", updated.name());
        assertEquals("Avenida Central, 999", updated.address());
        assertEquals("Brasileira", updated.cuisineType());
        assertEquals("10h às 22h", updated.openingHours());
        assertEquals(owner.id(), updated.ownerId());
    }

    @Test
    void shouldDeleteRestaurantUsingRealDatabase() {
        UserOutputData owner = createOwnerUser();
        RestaurantOutputData created = createRestaurant(owner.id());

        restaurantUseCase.delete(created.id());

        assertFalse(restaurantRepository.existsById(created.id()));
    }

    @Test
    void shouldThrowWhenRestaurantDoesNotExist() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> restaurantUseCase.findById(999L)
        );
    }

    @Test
    void shouldThrowWhenOwnerDoesNotExist() {
        assertThrows(
                ResourceNotFoundException.class,
                () -> restaurantUseCase.create(
                        new CreateRestaurantInputData(
                                "Restaurante Sem Dono",
                                "Rua Teste, 123",
                                "Brasileira",
                                "10h às 22h",
                                999L
                        )
                )
        );
    }

    @Test
    void shouldThrowWhenOwnerIsClient() {
        UserOutputData client = createClientUser();

        assertThrows(
                IllegalArgumentException.class,
                () -> restaurantUseCase.create(
                        new CreateRestaurantInputData(
                                "Restaurante Inválido",
                                "Rua Teste, 123",
                                "Brasileira",
                                "10h às 22h",
                                client.id()
                        )
                )
        );
    }

    private RestaurantOutputData createRestaurant(Long ownerId) {
        return restaurantUseCase.create(
                new CreateRestaurantInputData(
                        "Cantina Bella",
                        "Rua das Flores, 123",
                        "Italiana",
                        "11h às 23h",
                        ownerId
                )
        );
    }

    private UserOutputData createOwnerUser() {
        UserTypeOutputData ownerType = userTypeUseCase.create(
                new CreateUserTypeInputData("Dono de Restaurante")
        );

        return userUseCase.create(
                new CreateUserInputData(
                        "João Dono",
                        "joao.dono@email.com",
                        ownerType.id()
                )
        );
    }

    private UserOutputData createClientUser() {
        UserTypeOutputData clientType = userTypeUseCase.create(
                new CreateUserTypeInputData("Cliente")
        );

        return userUseCase.create(
                new CreateUserInputData(
                        "Maria Cliente",
                        "maria.cliente@email.com",
                        clientType.id()
                )
        );
    }
}
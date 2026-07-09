package com.restaurant.management.integration.controller;

import com.restaurant.management.infrastructure.persistence.repository.MenuItemJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.RestaurantJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserJpaRepository;
import com.restaurant.management.infrastructure.persistence.repository.UserTypeJpaRepository;
import com.restaurant.management.integration.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantControllerIntegrationTest extends AbstractIntegrationTest {

    RestaurantControllerIntegrationTest(
            MenuItemJpaRepository menuItemRepository,
            RestaurantJpaRepository restaurantRepository,
            UserJpaRepository userRepository,
            UserTypeJpaRepository userTypeRepository
    ) {
        super(menuItemRepository, restaurantRepository, userRepository, userTypeRepository);
    }

    @Test
    void shouldCreateRestaurant() {
        Integer ownerId = createOwnerUser();

        Map<?, ?> response = restClient.post()
                .uri("/api/restaurants")
                .body(Map.of(
                        "name", "Cantina Bella",
                        "address", "Rua das Flores, 123",
                        "cuisineType", "Italiana",
                        "openingHours", "11h às 23h",
                        "ownerId", ownerId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertNotNull(response.get("id"));
        assertEquals("Cantina Bella", response.get("name"));
        assertEquals("Rua das Flores, 123", response.get("address"));
        assertEquals("Italiana", response.get("cuisineType"));
        assertEquals("11h às 23h", response.get("openingHours"));
        assertEquals(ownerId, response.get("ownerId"));
        assertEquals("João Dono", response.get("ownerName"));
    }

    @Test
    void shouldFindAllRestaurants() {
        Integer ownerId = createOwnerUser();
        createRestaurant(ownerId);

        List<?> response = restClient.get()
                .uri("/api/restaurants")
                .retrieve()
                .body(List.class);

        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void shouldFindRestaurantById() {
        Integer ownerId = createOwnerUser();
        Integer restaurantId = createRestaurant(ownerId);

        Map<?, ?> response = restClient.get()
                .uri("/api/restaurants/{id}", restaurantId)
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(restaurantId, response.get("id"));
        assertEquals("Cantina Bella", response.get("name"));
        assertEquals(ownerId, response.get("ownerId"));
        assertEquals("João Dono", response.get("ownerName"));
    }

    @Test
    void shouldUpdateRestaurant() {
        Integer ownerId = createOwnerUser();
        Integer restaurantId = createRestaurant(ownerId);

        Map<?, ?> response = restClient.put()
                .uri("/api/restaurants/{id}", restaurantId)
                .body(Map.of(
                        "name", "Cantina Atualizada",
                        "address", "Avenida Central, 999",
                        "cuisineType", "Brasileira",
                        "openingHours", "10h às 22h",
                        "ownerId", ownerId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(response);
        assertEquals(restaurantId, response.get("id"));
        assertEquals("Cantina Atualizada", response.get("name"));
        assertEquals("Avenida Central, 999", response.get("address"));
        assertEquals("Brasileira", response.get("cuisineType"));
        assertEquals("10h às 22h", response.get("openingHours"));
        assertEquals(ownerId, response.get("ownerId"));
    }

    @Test
    void shouldDeleteRestaurant() {
        Integer ownerId = createOwnerUser();
        Integer restaurantId = createRestaurant(ownerId);

        restClient.delete()
                .uri("/api/restaurants/{id}", restaurantId)
                .retrieve()
                .toBodilessEntity();

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.get()
                        .uri("/api/restaurants/{id}", restaurantId)
                        .retrieve()
                        .body(Map.class)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnBadRequestWhenOwnerIsClient() {
        Integer clientTypeId = createUserType("Cliente");
        Integer clientId = createUser("Maria Cliente", "maria.cliente@email.com", clientTypeId);

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
                        .uri("/api/restaurants")
                        .body(Map.of(
                                "name", "Restaurante Inválido",
                                "address", "Rua Teste, 123",
                                "cuisineType", "Brasileira",
                                "openingHours", "10h às 22h",
                                "ownerId", clientId
                        ))
                        .retrieve()
                        .toBodilessEntity()
        );

        assertEquals(400, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenOwnerDoesNotExist() {
        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
                        .uri("/api/restaurants")
                        .body(Map.of(
                                "name", "Restaurante Sem Dono",
                                "address", "Rua Teste, 123",
                                "cuisineType", "Brasileira",
                                "openingHours", "10h às 22h",
                                "ownerId", 999
                        ))
                        .retrieve()
                        .toBodilessEntity()
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnBadRequestWhenRestaurantNameIsBlank() {
        Integer ownerId = createOwnerUser();

        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.post()
                        .uri("/api/restaurants")
                        .body(Map.of(
                                "name", "",
                                "address", "Rua Teste, 123",
                                "cuisineType", "Brasileira",
                                "openingHours", "10h às 22h",
                                "ownerId", ownerId
                        ))
                        .retrieve()
                        .toBodilessEntity()
        );

        assertEquals(400, exception.getStatusCode().value());
    }

    @Test
    void shouldReturnNotFoundWhenRestaurantDoesNotExist() {
        RestClientResponseException exception = assertThrows(
                RestClientResponseException.class,
                () -> restClient.get()
                        .uri("/api/restaurants/{id}", 999)
                        .retrieve()
                        .body(Map.class)
        );

        assertEquals(404, exception.getStatusCode().value());
    }

    private Integer createOwnerUser() {
        Integer ownerTypeId = createUserType("Dono de Restaurante");
        return createUser("João Dono", "joao.dono@email.com", ownerTypeId);
    }

    private Integer createUserType(String name) {
        Map<?, ?> userType = restClient.post()
                .uri("/api/user-types")
                .body(Map.of("name", name))
                .retrieve()
                .body(Map.class);

        assertNotNull(userType);
        return (Integer) userType.get("id");
    }

    private Integer createUser(String name, String email, Integer userTypeId) {
        Map<?, ?> user = restClient.post()
                .uri("/api/users")
                .body(Map.of(
                        "name", name,
                        "email", email,
                        "userTypeId", userTypeId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(user);
        return (Integer) user.get("id");
    }

    private Integer createRestaurant(Integer ownerId) {
        Map<?, ?> restaurant = restClient.post()
                .uri("/api/restaurants")
                .body(Map.of(
                        "name", "Cantina Bella",
                        "address", "Rua das Flores, 123",
                        "cuisineType", "Italiana",
                        "openingHours", "11h às 23h",
                        "ownerId", ownerId
                ))
                .retrieve()
                .body(Map.class);

        assertNotNull(restaurant);
        return (Integer) restaurant.get("id");
    }
}